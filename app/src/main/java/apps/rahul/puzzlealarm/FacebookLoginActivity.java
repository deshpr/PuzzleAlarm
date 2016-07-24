package apps.rahul.puzzlealarm;

import android.graphics.BitmapFactory;
import android.hardware.camera2.params.Face;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.Log;
import android.content.Intent;
import android.widget.Button;
import android.net.Uri;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.app.AlarmManager;
import android.content.Context;
import android.app.PendingIntent;


import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.net.URL;
import java.util.Calendar;

import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookCallback;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class FacebookLoginActivity extends AppCompatActivity {



    public static final String PUZZLE_BITMAP_URL_KEY = "puzzleBitmapUrl";





    private class GetBitmapAsync implements Helper.PostHandleAsyncTaskGetBitmap
    {
        public void toExecuteDelegate(Bitmap bitmap)
        {
            facebookPicture.setImageBitmap(bitmap);
        }
    }




    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;





    private void setUpAlarm()
    {

        Button button =  (Button)this.findViewById(R.id.setAlarm);
        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                alarmManager = (AlarmManager)FacebookLoginActivity.this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(FacebookLoginActivity.this, FacebookLoginActivity.class);
                alarmIntent = PendingIntent.getBroadcast(FacebookLoginActivity.this, 0,intent, 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 7);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,alarmIntent);

            }
        });

    }


    private Bitmap imageToPass;
    private Button startPuzzleActivity;
    private String urlOfPuzzleBitmap;

    private void launchPuzzleActivity()
    {
        Intent intent = new Intent(FacebookLoginActivity.this, PuzzleActivity.class);
        intent.putExtra(PUZZLE_BITMAP_URL_KEY, urlOfPuzzleBitmap);
        startActivity(intent);
    }

    private LoginButton loginButton;
    private CallbackManager callBackManager;
    private ImageView facebookPicture;

    private int user_id;
    private AccessToken userToken;

    private void getImageFromUrl(String url)
    {
        Helper.GetBitmapAsync(new GetBitmapAsync(), url);
//        Helper.GetPictureFromUrlAsync asyncTask =  new Helper.GetPictureFromUrlAsync();
 //       asyncTask.asyncTaskHandler = new GetBitmapAsync();
  //      asyncTask.execute(url);
    }

    private void obtainFacebookPhoto(String id)
    {
        String requestString = "/" + id;

        GraphRequest request =  GraphRequest.newGraphPathRequest(
                userToken,
                requestString,
                new GraphRequest.Callback(){
                    @Override
                    public void onCompleted(GraphResponse response)
                    {
                        JSONObject result = response.getJSONObject();
                        Log.d(PuzzleActivity.TAG, "Result of pic = " + response.getJSONObject());
                        try
                        {
                            JSONArray differentImages = result.getJSONArray("webp_images");
                            JSONObject image = differentImages.getJSONObject(0);
                            Log.d(PuzzleActivity.TAG, "Image to display = " + image);
                            getImageFromUrl(image.getString("source"));
                            urlOfPuzzleBitmap = image.getString("source");
                                             }
                        catch(org.json.JSONException ex)
                        {
                            Log.d(PuzzleActivity.TAG, ex.toString());
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "height,width,webp_images");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void getPictures()
    {
        Log.d(PuzzleActivity.TAG, "Get the pictures");
        GraphRequest request = GraphRequest.newGraphPathRequest(
                userToken,
                "/" + userToken.getUserId() + "/photos",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                   Log.d(PuzzleActivity.TAG, "Result = " + response.getJSONObject());
                        JSONObject  pictureResponse = response.getJSONObject();
                        try
                        {
                            JSONArray picDataArray = pictureResponse.getJSONArray("data");
                            Log.d(PuzzleActivity.TAG, "There are " + picDataArray.length() + " items");
                            Random random = new  Random();
                            int randomPosition =  Helper.getRandomNumber(0, picDataArray.length());
                            JSONObject pictureInfo =  picDataArray.getJSONObject(randomPosition);
                            Log.d(PuzzleActivity.TAG, "Index  = "  +randomPosition + " and id = " + pictureInfo.get("id"));
                            // now obtain picture for this

                            obtainFacebookPhoto(pictureInfo.get("id").toString());

                        }catch(org.json.JSONException ex)
                        {
                            Log.d(PuzzleActivity.TAG, "Exception = " + ex.toString());
                        }

                    }
                });

        request.executeAsync();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_login);
        AppEventsLogger.activateApp(this);
        Button button = (Button)findViewById(R.id.getData);
        startPuzzleActivity =(Button)this.findViewById(R.id.startPuzzleActivityButton);
        startPuzzleActivity.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
           {
               launchPuzzleActivity();
           }
        });
        facebookPicture = (ImageView)this.findViewById(R.id.facebookPicture);

        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                getPictures();
            }
        });

        callBackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)this.findViewById(R.id.facebookLoginButton);

        // loginButton.setAct
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(Application.TAG, "Clicked Facebook Sign In");
                    loginButton.setReadPermissions("email");
                   loginButton.setReadPermissions("user_photos");
                Log.d(Application.TAG, "Assigned the permissions");
//                FacebookContentProvider provider = new FacebookContentProvider();
 //               provider.setUpUI(loginButton);
   //             Application.loggedInProviders.add(provider);
            }
        });
        loginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult result)
            {
                 userToken = result.getAccessToken();
                Log.d(PuzzleActivity.TAG, "Succesful! Id = " + userToken.getUserId() + " and permissions " + userToken.getPermissions()
                         + " and expiry = " + userToken.getExpires());

            }

            @Override
            public void onCancel(){
                Log.d(PuzzleActivity.TAG, "Login canceled");
            }

            @Override
            public void onError(FacebookException ex)
            {
             Log.d(PuzzleActivity.TAG, "Error logging in");
            }
        });

        setUpAlarm();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        callBackManager.onActivityResult(requestCode, resultCode, intent);
    }


}

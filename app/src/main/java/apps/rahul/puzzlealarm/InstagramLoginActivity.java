package apps.rahul.puzzlealarm;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rahul on 7/23/2016.
 */
public class InstagramLoginActivity extends AppCompatActivity {

    private String  instagramUserToken;

    public void onComplete(String accessToken)
    {
        this.instagramUserToken = accessToken;
        Log.d(PuzzleActivity.TAG, "Acquired the auth token from Instagram");
    }



    public void onError(String error)
    {
        Log.d(PuzzleActivity.TAG, "Error = " + error);
    }

    private InstagramContentProvider client;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagramlogin);
        Button instagramLogin = (Button)this.findViewById(R.id.instagramLogin);
        instagramLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                client = new InstagramContentProvider(InstagramLoginActivity.this);
                Log.d(PuzzleActivity.TAG, "Created  the client");
            }
        });

        Button getUserMedia = (Button)this.findViewById(R.id.instagramGetUserMedia);
        /*
        getUserMedia.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                client.getCurrentUserMedia(instagramUserToken, Helper.InstagramClientCallback() {
                    @Override
                    public void onComplete(String result) {
                        Log.d(PuzzleActivity.TAG, "User media json = "  + result);
                        try
                        {
                            JSONObject  jsonResult = new JSONObject(result);
                            JSONArray pictureData = jsonResult.getJSONArray("data");
                            int size = pictureData.length();
                            Log.d(PuzzleActivity.TAG, "There are " + pictureData.length() + " content ");
                            int random  =  Helper.getRandomNumber(0, size);
                            JSONObject picture = (JSONObject) pictureData.get(random);
                            JSONObject imageData = picture.getJSONObject("images");
                            JSONObject standardResolution = imageData.getJSONObject("standard_resolution");
                            Log.d(PuzzleActivity.TAG, "Standard Resolution = "+ standardResolution.toString());
                            String url = standardResolution.getString("url");
                            Helper.GetBitmapAsync(new Helper.PostHandleAsyncTaskGetBitmap() {
                                @Override
                                public void toExecuteDelegate(Bitmap bitmap) {
                                    ((ImageView)InstagramLoginActivity.this.findViewById(R.id.instagramPicture))
                                                    .setImageBitmap(bitmap);
                                }
                            }, url);
                        }catch(org.json.JSONException ex)
                        {
                            Log.d(PuzzleActivity.TAG, "Exception = " + ex.toString());
                        }
                    }
                    @Override
                    public void onError(String errorInfo) {
                        Log.d(PuzzleActivity.TAG, "Getting media did not work " + errorInfo);
                    }
                });
            }
        }); */
    }

}

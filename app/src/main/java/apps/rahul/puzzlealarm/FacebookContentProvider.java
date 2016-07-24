package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class FacebookContentProvider implements IContentProvider, Helper.PostHandleAsyncTaskGetBitmap {

    private CallbackManager callbackManager;
    private String userToken;
    private AccessToken  facebookAccessToken;
    private Context mContext;
    private LoginButton loginButton;
    private Helper.PostHandleAsyncTaskGetBitmap postHandleAsyncTaskGetBitmap;
    private Bitmap bitmap;

    public void toExecuteDelegate(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        Log.d(Application.TAG, "FacebookContentProvider received bitmap");
        postHandleAsyncTaskGetBitmap.toExecuteDelegate(bitmap);
    }


    public  String getContentProviderId()
    {
        return "Facebook";
    }

    public FacebookContentProvider(Activity callingActivity, Context context)
    {
        FacebookSdk.sdkInitialize(callingActivity);
        AppEventsLogger.activateApp(context);
        callbackManager = CallbackManager.Factory.create();
    }

    public void setUpUI(View v) {
        if (v instanceof LoginButton) {
            loginButton = (LoginButton) v;
            loginButton.setReadPermissions("email");
            loginButton.setReadPermissions("user_photos");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
            {
                @Override
                public void onSuccess(LoginResult result)
                {
                    facebookAccessToken = result.getAccessToken();
                    Log.d(PuzzleActivity.TAG, "Succesful! Id = " + facebookAccessToken.getUserId() + " and permissions " + facebookAccessToken.getPermissions()
                            + " and expiry = " + facebookAccessToken.getExpires());
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

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(Application.TAG, "FacebookContentProvider does activit result");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public String getAuthToken()
    {
        return this.facebookAccessToken.getToken();
    }

    private void obtainFacebookPhoto(String id)
    {
        String requestString = "/" + id;

        GraphRequest request =  GraphRequest.newGraphPathRequest(
                facebookAccessToken,
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
                            Helper.GetBitmapAsync(FacebookContentProvider.this,
                                        image.getString("source"));
                                        //                getImageFromUrl(image.getString("source"));
            //                urlOfPuzzleBitmap = image.getString("source");
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
                facebookAccessToken,
                "/" + facebookAccessToken.getUserId() + "/photos",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        Log.d(PuzzleActivity.TAG, "Result = " + response.getJSONObject());
                        JSONObject pictureResponse = response.getJSONObject();
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


    public Bitmap getCurrentBitmap()
    {
        return this.bitmap;
    }

    public void getBitmapAsync(Helper.PostHandleAsyncTaskGetBitmap instance)
    {
        Log.d(Application.TAG, "FBContentProvider is fetching pics");
        this.postHandleAsyncTaskGetBitmap = instance;
        this.getPictures();
    }

}

package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import android.content.Intent;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONObject;


public class InstagramContentProvider implements IContentProvider{


    private InstagramLoginDialog instagramLoginDialog;
    private String instagramAccessToken;
    private String authenticationUrl;
    private Context mContext;
    private Bitmap currentBitmap;

    private static final String INSTAGRAM_AUTH_URL_PREFIX = "https://api.instagram.com/";

    public InstagramContentProvider(Activity callingActivity)
    {
        this.mContext = callingActivity;
        this.authenticationUrl = INSTAGRAM_AUTH_URL_PREFIX + "oauth/authorize/" +
                "?client_id=" + mContext.getString(R.string.INSTAGRAM_CLIENT_ID)
                + "&redirect_uri=" + mContext.getString(R.string.INSTAGRAM_REDIRECT_URL) + "&response_type=token&display=touch";
        instagramLoginDialog  = new InstagramLoginDialog(mContext , authenticationUrl, new Helper.PostAsyncTaskCallback()
        {
            @Override
            public void onComplete(Object... object)
            {
                Log.d(Application.TAG, "InstagramContentProvider received the auth token");
                if(object.length == 1  && (object instanceof String[]))
                {
                    InstagramContentProvider.this.instagramAccessToken = (String)object[0];
                    Log.d(Application.TAG, "Instagram obtained the authorization token");
                }
            }

            @Override
            public void onError(Object error)
            {
                Log.d(Application.TAG, "There was an error when getting the Instafram token");
                Log.d(Application.TAG, error.toString());
            }

        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Do nothing....
    }


    public void setUpUI(View v)
    {
        instagramLoginDialog.show();
    }

    public Bitmap getCurrentBitmap()
    {
        return currentBitmap;
    }

    public String getAuthToken()
    {
        return this.instagramAccessToken;
    }

    public void getCurrentUserMedia(String authorizationToken, final Helper.PostAsyncTaskCallback client)
    {
        RequestQueue requestQueue  = Volley.newRequestQueue(mContext);
        String url = INSTAGRAM_AUTH_URL_PREFIX + "v1/users/self/media/recent/" + "?access_token=" + authorizationToken;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d(PuzzleActivity.TAG, "Client got media " + response);
                            client.onComplete(new String[]{response});
                    }

                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        client.onError(new String[]{error.getMessage()});
                    }
                });
        requestQueue.add(request);
    }


    private String getUrlFromJson(String jsonString)
    {
        String url= "";
        try
        {
            JSONObject  jsonContent = new JSONObject(jsonString);
            JSONArray pictureData = jsonContent .getJSONArray("data");
            int size = pictureData.length();
            Log.d(PuzzleActivity.TAG, "There are " + pictureData.length() + " content ");
            int random  =  Helper.getRandomNumber(0, size);
            JSONObject picture = (JSONObject) pictureData.get(random);
            JSONObject imageData = picture.getJSONObject("images");
            JSONObject standardResolution = imageData.getJSONObject("standard_resolution");
            Log.d(PuzzleActivity.TAG, "Standard Resolution = "+ standardResolution.toString());
            url = standardResolution.getString("url");
        }
        catch(org.json.JSONException ex)
        {

        }
        return url;
    }

    public void getBitmapAsync(final Helper.PostHandleAsyncTaskGetBitmap callback)
    {
        this.getCurrentUserMedia(this.getAuthToken(), new Helper.PostAsyncTaskCallback() {
            @Override
            public void onComplete(Object... object) {
                Log.d(Application.TAG, "Instagram obtained url of image " + object);
                if(object instanceof  String[] && object.length >0){
                    String  urlOfBitmap = getUrlFromJson((String)object[0]);
                    Log.d(Application.TAG, "Url of bitmap instagram =" + urlOfBitmap);
                    Helper.GetBitmapAsync(new Helper.PostHandleAsyncTaskGetBitmap() {
                        @Override
                        public void toExecuteDelegate(Bitmap bitmap) {
                            Log.d(Application.TAG, "Obtained the bitmap for instagram");
                            InstagramContentProvider.this.currentBitmap = bitmap;
                            callback.toExecuteDelegate(bitmap);
                        }
                    }, urlOfBitmap);
                }
            }

            @Override
            public void onError(Object errorInfo) {

            }
        });
    }
}

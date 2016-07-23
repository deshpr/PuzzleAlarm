package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

public class InstagramClient{


    private InstagramLoginDialog instagramLoginDialog;
    private String instagramAccessToken;
    private String authenticationUrl;

    private Context mContext;

    //   private static final String INSTAGRAM_AUTH_URL_PREFIX = "https://api.instagram.com/oauth/authorize/?client_id=CLIENT-ID&redirect_uri=REDIRECT-URI&response_type=code";
    private static final String INSTAGRAM_AUTH_URL_PREFIX = "https://api.instagram.com/";

    public void getCurrentUserMedia(String authorizationToken, final InstagramClientCallback client)
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
                            client.onComplete(response);
                    }

                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        client.onError(error.getMessage());
                    }
                });
        requestQueue.add(request);
    }





    public interface InstagramClientCallback
    {
        public  void onComplete(String  result);
        public void onError(String errorInfo);
    }



    public String getOAuthToken()
    {
        return this.instagramAccessToken;
    }

    public InstagramClient(Context context, InstagramClientCallback listener)
    {
        this.mContext = context;
        this.authenticationUrl = INSTAGRAM_AUTH_URL_PREFIX + "oauth/authorize/" +
                                "?client_id=" + context.getString(R.string.INSTAGRAM_CLIENT_ID)
                             + "&redirect_uri=" + context.getString(R.string.INSTAGRAM_REDIRECT_URL) + "&response_type=token&display=touch";
        instagramLoginDialog  = new InstagramLoginDialog(context , authenticationUrl, listener);
        instagramLoginDialog.show();

    }

}

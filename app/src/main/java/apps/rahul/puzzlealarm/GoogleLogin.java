package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/22/2016.
 */

import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gdata.util.ServiceException;
import com.google.gdata.data.photos.UserFeed;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;


public class GoogleLogin extends AppCompatActivity implements  OnConnectionFailedListener, View.OnClickListener{


    private final int SIGN_IN = 111;
    private String userId;
    private GoogleSignInOptions options;
    private GoogleApiClient apiClient;
    private SignInButton  googleSignIn;
    private TextView userName;

    private TextView albumNames;

    private PicasawebService picasaWebService;
   private  final String API_END_POINT = "https://picasaweb.google.com/data/feed/api/user/";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_googlelogin);
        userName = (TextView)this.findViewById(R.id.userName);
        albumNames = (TextView)this.findViewById(R.id.albumTextView);
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                    .build();
        googleSignIn  = (SignInButton)this.findViewById(R.id.googleSignInButton);
        googleSignIn.setOnClickListener(this);

    }



    private void  googleSignIn()
    {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        this.startActivityForResult(googleSignInIntent, SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN)
        {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(signInResult);
        }
    }

    private List<AlbumEntry> getGooglePlusPicasaAlbums(String  userId)
    {
        List<AlbumEntry> albums = null;
        String url = API_END_POINT  + userId;
        try
        {
            UserFeed  picasaUserFeed = getPicasaFeed(url, UserFeed.class);
            List<GphotoEntry>  gphotoEntries = picasaUserFeed.getEntries();
            albums = new ArrayList<AlbumEntry>();
            for(GphotoEntry gphotoEntry : gphotoEntries)
            {
                AlbumEntry albumEntry = new AlbumEntry(gphotoEntry);
                albums.add(albumEntry);
//                albumNames.setText(albumNames.getText() + " ," + albumEntry.getTitle().getPlainText());
            }
        }
        catch(IOException ex)
        {

        }
        catch(ServiceException ex)
        {

        }
        return albums;

    }


    public <T extends GphotoFeed> T getPicasaFeed(String href, Class<T> feedClass) throws IOException, ServiceException{
        if(picasaWebService!=null)
            return picasaWebService.getFeed(new URL(href), feedClass);
        return null;
    }

    private void initiatePicasaWebService(String authorizationToken)
    {
            picasaWebService  = new PicasawebService("pictureframe");
            picasaWebService.setUserToken(authorizationToken);
           new AsyncTask<Void, Void, List<AlbumEntry>>()
           {

                @Override
               public List<AlbumEntry> doInBackground(Void ...voids)
                {
                    List<AlbumEntry> albums = getGooglePlusPicasaAlbums(userId);
                    return albums;
                }

               @Override
               public void onPostExecute(List<AlbumEntry> albums)
               {

                    for(AlbumEntry entry : albums)
                    {
                        albumNames.setText(albumNames.getText() + ", " + entry.getTitle().getPlainText());
                    }
               }
           }.execute();

    }

    private void handleGoogleSignInResult(GoogleSignInResult result)
    {
        Log.d(PuzzleActivity.TAG, "Handling sign in result");
        if(result.getStatus()!=null)
            Log.d(PuzzleActivity.TAG, "contents = " + result.getStatus().describeContents() + " and " + result.getStatus());
        if(result.isSuccess())
        {

            Log.d(PuzzleActivity.TAG, "Log in was sucessful");
            GoogleSignInAccount googleSignedInAccount =  result.getSignInAccount();
            userName.setText(googleSignedInAccount.getEmail() + " and Id = " + googleSignedInAccount.getDisplayName() +
                " and user id = " + googleSignedInAccount.getId());
            userId = googleSignedInAccount.getId();
            initiatePicasaWebService(googleSignedInAccount.getIdToken());
        }
        else
        {
            Log.d(PuzzleActivity.TAG, "Login did not work");
            Log.d(PuzzleActivity.TAG, result.toString());
        }
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.googleSignInButton:
                Log.d(PuzzleActivity.TAG, "Google Sign In button clicked");
                googleSignIn();
                break;
        }
    }


    public void onConnectionFailed(ConnectionResult result)
    {
        Log.d(PuzzleActivity.TAG, "Google connection failed = " + result.toString());
    }

}

package apps.rahul.puzzlealarm;


import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;


import com.onedrive.sdk.authentication.MSAAuthenticator;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.IClientConfig;
import com.onedrive.sdk.core.DefaultClientConfig;
import com.onedrive.sdk.extensions.ISearchCollectionPage;
import com.onedrive.sdk.extensions.ISearchRequest;
import com.onedrive.sdk.extensions.ISearchRequestBuilder;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.extensions.OneDriveClient;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.Drive;
import com.onedrive.sdk.concurrency.ICallback;


import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    private Button loginOneDrive;

    private MSAAuthenticator oneDriveAuthenticator;
    private IOneDriveClient oneDriveClient;
    private static final String Client_Id = "9d8955b9-742d-4d73-9a47-1cf7b50c96da";

    private Button getClientDriveButton;


    private Button getPicturesButton;

    private ImageView oneDriveBitmap;

    private class GetBitmapAsyncPostHandler implements Helper.PostHandleAsyncTaskGetBitmap {

        public void toExecuteDelegate(Bitmap bitmap)
        {
            Log.d(PuzzleActivity.TAG, "OneDrive bitmap obtained");

            oneDriveBitmap = (ImageView)LoginActivity.this.findViewById(R.id.oneDriveImage);
            oneDriveBitmap.setImageBitmap(Bitmap.createScaledBitmap(bitmap, oneDriveBitmap.getWidth(), oneDriveBitmap.getHeight(), false));

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginOneDrive = (Button)this.findViewById(R.id.oneDriveLogin);
        loginOneDrive.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v) {
               oneDriveAuthenticator = new MSAAuthenticator() {
                   @Override
                   public String getClientId() {
                       return Client_Id;
                   }

                   @Override
                   public String[] getScopes() {
                       return new String[]{"onedrive.readonly"};
                   }
               };


               // Get the OneDrive Client object.

               ICallback<IOneDriveClient> callback = new ICallback<IOneDriveClient>() {
                   @Override
                   public void success(IOneDriveClient oneDriveClient) {
                       Log.d(PuzzleActivity.TAG, "It worked!");
                       LoginActivity.this.oneDriveClient = oneDriveClient;
                   }

                   @Override
                   public void failure(ClientException ex) {
                    Log.d(PuzzleActivity.TAG, "There was an exception!");
                   }
               };

               final IClientConfig oneDriveConfiguration = DefaultClientConfig.createWithAuthenticator(oneDriveAuthenticator);
               new OneDriveClient.Builder()
                       .fromConfig(oneDriveConfiguration)
                       .loginAndBuildClient(LoginActivity.this, callback);
           }
           });

        getClientDriveButton = (Button)this.findViewById(R.id.getDriveButton);
        getClientDriveButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(oneDriveClient!=null)
                {
                    oneDriveClient
                            .getDrive()
                            .buildRequest()
                            .get(new ICallback<Drive>()
                            {
                                @Override
                                public void success(Drive result)
                                {
                                    String message = "Drive id = " + result.id + " and type = " + result.driveType +
                                             "owner = " + result.owner;
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Log.d(PuzzleActivity.TAG, result.getRawObject().toString());
                                }

                                @Override
                                public void failure(ClientException ex) {
                                    Log.d(PuzzleActivity.TAG, "Exception = " + ex.toString());
                                }

                            });
                }
            }

        });


        getPicturesButton = (Button)this.findViewById(R.id.getPictures);
        getPicturesButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
               ISearchRequest request =  oneDriveClient
                        .getDrive()
                        .getRoot()
                        .getSearch(".jpg")

                        .buildRequest();
                Log.d(PuzzleActivity.TAG, request.toString());
                request.get(new ICallback<ISearchCollectionPage>() {
                            @Override
                            public void success(ISearchCollectionPage iSearchCollectionPage) {

                                Log.d(PuzzleActivity.TAG, "Results are = "+ iSearchCollectionPage.getRawObject().toString());

                                com.google.gson.JsonObject result = iSearchCollectionPage.getRawObject();
                                JsonArray images  = result.getAsJsonArray("value");

                                int count =images.size();
                                Log.d(PuzzleActivity.TAG, "There are " + count + " itmes!");
                                int randomPictureIndex = Helper.getRandomNumber(0, count);
                                JsonObject randomPicture = images.get(randomPictureIndex).getAsJsonObject();
                                String downloadUrl = randomPicture.get("@content.downloadUrl").getAsString();
                                Log.d(PuzzleActivity.TAG, downloadUrl);
                                Helper.GetBitmapAsync(new GetBitmapAsyncPostHandler(), downloadUrl);

                            }



                            @Override
                            public void failure(ClientException ex) {
                                Log.d(PuzzleActivity.TAG, "Did not work  = " + ex.toString());
                            }
                        });

            }
        });

    }
}

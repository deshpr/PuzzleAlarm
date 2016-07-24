package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/24/2016.
 */

import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onedrive.sdk.authentication.MSAAuthenticator;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.DefaultClientConfig;
import com.onedrive.sdk.core.IClientConfig;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.ISearchCollectionPage;
import com.onedrive.sdk.extensions.ISearchRequest;
import com.onedrive.sdk.extensions.OneDriveClient;

public class OneDriveContentProvider implements IContentProvider, Helper.PostHandleAsyncTaskGetBitmap {

    private Context mContext;
    private String userToken;
    private Activity callingActivity;

    private Helper.PostHandleAsyncTaskGetBitmap callingActivityHandler;
    private MSAAuthenticator oneDriveAuthenticator;
    private IOneDriveClient oneDriveClient;
    private static final String Client_Id = "9d8955b9-742d-4d73-9a47-1cf7b50c96da";

    public OneDriveContentProvider(Activity callingActivity)
    {
        mContext = callingActivity;
        this.callingActivity = callingActivity;
    }

    private Bitmap currentBitmap;


    public void toExecuteDelegate(Bitmap bitmap)
    {
        currentBitmap = bitmap;
        callingActivityHandler.toExecuteDelegate(bitmap);
    }

    public void getClientDrive()
    {

    }

    public void getPictures(final Helper.PostHandleAsyncTaskGetBitmap postHandleAsyncTaskGetBitmap)
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
                Helper.GetBitmapAsync(postHandleAsyncTaskGetBitmap, downloadUrl);
            }

            @Override
            public void failure(ClientException ex) {
                Log.d(PuzzleActivity.TAG, "Did not work  = " + ex.toString());
            }
        });


    }



    public void setUpUI(View v)
    {
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
                OneDriveContentProvider.this.oneDriveClient = oneDriveClient;
            }

            @Override
            public void failure(ClientException ex) {
                Log.d(PuzzleActivity.TAG, "There was an exception!");
            }
        };

        final IClientConfig oneDriveConfiguration = DefaultClientConfig.createWithAuthenticator(oneDriveAuthenticator);
        new OneDriveClient.Builder()
                .fromConfig(oneDriveConfiguration)
                .loginAndBuildClient(callingActivity, callback);

    }

    public String getAuthToken()
    {
        return userToken;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Do nothing...
    }

    public void getBitmapAsync(Helper.PostHandleAsyncTaskGetBitmap  callbackInstance)
    {
        this.callingActivityHandler = callbackInstance;
        this.getPictures(this);

    }

    public Bitmap getCurrentBitmap()
    {
        return currentBitmap;
    }

}

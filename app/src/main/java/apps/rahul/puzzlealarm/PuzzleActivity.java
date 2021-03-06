package apps.rahul.puzzlealarm;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import java.io.InputStream;
import java.net.URL;




import android.util.Log;
import android.content.Intent;
import android.view.MotionEvent;
import android.net.Uri;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.graphics.Point;

import com.facebook.FacebookOperationCanceledException;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;



public class PuzzleActivity extends AppCompatActivity {


    public static final int CODE = 101;

    public static String TAG = "PUZZLEAPP";

    public File path;
    public String value;
    private PuzzleCanvas imageCanvas;
    private Paint paint;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
 //   private GoogleApiClient client;


    private FrameLayout puzzleGameFrame;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataObtained) {
        System.gc();
        Log.d(TAG, "Obtained the result");
        if (requestCode == CODE && resultCode == RESULT_OK) {
//            Uri uri = dataObtained.getData();
            Log.d(TAG, "Storing the result at value = " + value);
            Bitmap toShow = BitmapFactory.decodeFile(value);
            Log.d(TAG, toShow.toString());
        }
    }


    private PuzzleCanvas canvasGame;


    private class AsyncTaskHandler implements Helper.PostHandleAsyncTaskGetBitmap
    {
        public void toExecuteDelegate(Bitmap bitmap)
        {
            canvasGame.startGame(bitmap);
        }
    }


    private  void  getBitmapToShow(Bundle  bundleFromLaunchingActivity)
    {
        Intent launchingIntent = this.getIntent();
        String url = launchingIntent.getStringExtra(FacebookLoginActivity.PUZZLE_BITMAP_URL_KEY);

        Helper.GetBitmapAsync(new AsyncTaskHandler(), url);
        //   String url = bundleFromLaunchingActivity.getString(FacebookLoginActivity.PUZZLE_BITMAP_URL_KEY);
       // Helper.GetPictureFromUrlAsync getBitmapAsync = new Helper.GetPictureFromUrlAsync();
       // getBitmapAsync.asyncTaskHandler =  new  AsyncTaskHandler();
       // getBitmapAsync.execute(url);
        Log.d(PuzzleActivity.TAG, "obtaining bitmap for url =  " + url);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        puzzleGameFrame = (FrameLayout) this.findViewById(R.id.puzzleGameFrame);
        canvasGame = new PuzzleCanvas(this);
        Log.d(TAG, "Hwight = " + puzzleGameFrame.getHeight() + " and width = " + puzzleGameFrame.getWidth());
        puzzleGameFrame.addView(canvasGame);
  //      new GetPictureFromUrl().execute(bitmapUrl);
//        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Rahul.jpg");
 //       canvasGame.startGame(bitmap);
        Log.d(TAG, "Added the view");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
  //      client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        this.getBitmapToShow(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Puzzle Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://apps.rahul.puzzlealarm/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Puzzle Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://apps.rahul.puzzlealarm/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
}

package apps.rahul.puzzlealarm;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;
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

public class PuzzleActivity extends AppCompatActivity{


    public static final int CODE = 101;

    public static String TAG  = "PUZZLEAPP";

    public  File path;
    public String value;
    private PuzzleCanvas imageCanvas;
    private Paint paint;



    private void DisplayImageFromCameraIntent()
    {
        value = Environment.getExternalStorageDirectory() +  "/DCIM/Camera/Rahul.jpg";
        path = new File(value);
        Log.d(PuzzleActivity.TAG, "setting the image");
       // imageCanvas.setImageToShow(BitmapFactory.decodeFile(path.getAbsolutePath()));
//        Uri outputUri = Uri.fromFile(path);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 //       intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
 //       Log.d(TAG, "Starting intent fpr camera");
  //      startActivityForResult(intent, CODE);
    }


/*
    public boolean onTouch(View v, MotionEvent  motionEvent)
    {
        Log.d(TAG, "onTouch occurred");
        String message = "";
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                message = "DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                message = "MOVE";
                break;
            case MotionEvent.ACTION_UP:
                message = "UP";
                break;
        }
        Log.d(TAG, message);
     //   imageCanvas.drawLine(10,10,50,90, paint);
        return true;
    }
*/


    private FrameLayout puzzleGameFrame;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataObtained)
    {
        System.gc();
        Log.d(TAG, "Obtained the result");
        if(requestCode == CODE && resultCode == RESULT_OK){
//            Uri uri = dataObtained.getData();
            Log.d(TAG, "Storing the result at value = " + value);
            Bitmap toShow = BitmapFactory.decodeFile(value);
            Log.d(TAG, toShow.toString());
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        puzzleGameFrame = (FrameLayout)this.findViewById(R.id.puzzleGameFrame);
        PuzzleCanvas canvasGame = new PuzzleCanvas(this);
        Log.d(TAG, "Hwight = " + puzzleGameFrame.getHeight() + " and width = " + puzzleGameFrame.getWidth());
        puzzleGameFrame.addView(canvasGame);
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +  "/DCIM/Camera/Rahul.jpg");
        canvasGame.startGame(bitmap);
        Log.d(TAG, "Added the view");

    }



}

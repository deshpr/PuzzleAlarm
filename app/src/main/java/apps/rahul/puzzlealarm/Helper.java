package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/18/2016.
 */
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public  class Helper {


    static Random random;

    public  static int getRandomNumber(int start, int end)
    {
        random = new  Random();
        int value = random.nextInt(end);
        return value > start ? value : value + start;
    }

    public interface PostHandleAsyncTaskGetBitmap{
       public  void toExecuteDelegate(Bitmap bitmap);
    }


    public static void GetBitmapAsync(PostHandleAsyncTaskGetBitmap instance, String url)
    {
        Helper.GetPictureFromUrlAsync asyncTask =  new Helper.GetPictureFromUrlAsync();
        asyncTask.asyncTaskHandler = instance;
        asyncTask.execute(url);

    }

    private static class GetPictureFromUrlAsync extends AsyncTask<String, Void, Bitmap>
    {
        public PostHandleAsyncTaskGetBitmap asyncTaskHandler;

        public Bitmap  pictureBitmap;


        public static  GetPictureFromUrlAsync getInstance(){
            return new GetPictureFromUrlAsync();
        }


        protected Bitmap doInBackground(String... url)
        {
            try
            {
                //urlOfPuzzleBitmap = new URL(url[0]);
                return BitmapFactory.decodeStream((InputStream)(new URL(url[0]).getContent()));
            }
            catch(java.net.MalformedURLException ex)
            {
                Log.d(PuzzleActivity.TAG, ex.toString());
                return null;
            }
            catch(java.io.IOException ex)
            {
                Log.d(PuzzleActivity.TAG, ex.toString());
                return null;
            }
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            if(asyncTaskHandler!=null)
                asyncTaskHandler.toExecuteDelegate(bitmap);
            pictureBitmap = bitmap;
            Log.d(PuzzleActivity.TAG, "Obtained the image");
        }
    }
}

package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */

import android.graphics.Bitmap;
import android.content.Intent;
import android.view.View;

public interface IContentProvider {


//    public String getId();
    public void setUpUI(View v);
    public String getAuthToken();
    public Bitmap getCurrentBitmap();
    public void getBitmapAsync(Helper.PostHandleAsyncTaskGetBitmap  callbackInstance);
    public void onActivityResult(int requestCode, int resultCode, Intent data);



}

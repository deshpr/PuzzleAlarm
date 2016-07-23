/**
 * Created by Rahul on 7/23/2016.
 */

import android.graphics.Bitmap;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class FacebookContentProvider implements IContentProvider {


    public FacebookContentProvider(Context context)
    {
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(this);
    }


    public String getAuthToken()
    {
        return "";
    }

    public Bitmap getBitmap()
    {
        return null;
    }
}

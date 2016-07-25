package apps.rahul.puzzlealarm;

import android.content.BroadcastReceiver;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Rahul on 7/24/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(Application.TAG, "Alarm Receiver is working");
        ((SetAlarmActivity)SetAlarmActivity.getInstance()).notifyAlarmReceived();
    }


}

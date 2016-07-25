package apps.rahul.puzzlealarm;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Rahul on 7/24/2016.
 */
public class AlarmNotificationService extends IntentService {


    private NotificationManager notificationManager;

    public AlarmNotificationService()
    {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent)
    {
        displayNotificationMessageToUser("Rahul! Wake up!");
    }

    private void  displayNotificationMessageToUser(String notificationMessageToShow)
    {
        Log.d(Application.TAG, "AlarmNotificationService showing notification message");
        notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent  activityToLaunchOnClicking = PendingIntent.getActivity(this,
                                                        0,
                                                        new Intent(this, PuzzleActivity.class), 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                    .setContentTitle("Wake up!")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessageToShow))
                                    .setContentText(notificationMessageToShow);
        notificationBuilder.setContentIntent(activityToLaunchOnClicking);
        notificationManager.notify(1, notificationBuilder.build());
        Log.d(Application.TAG, "Notification has been sent");
    }

}

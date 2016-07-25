package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/24/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.view.View;
import android.widget.ToggleButton;
import android.app.Activity;

import com.google.android.gms.cast.Cast;

import java.util.Calendar;


public class SetAlarmActivity extends AppCompatActivity{


    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker timePicker;
    private  static AppCompatActivity instance;

    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(Application.TAG, "ONsTART of SetAlarm called");
        instance = this;
    }

    public static AppCompatActivity getInstance(){
        return instance;
    }

    public void notifyAlarmReceived()
    {
        Log.d(Application.TAG, "Make changes to UI on activity");
        ((android.widget.TextView)this.findViewById(R.id.alarmReceived)).setText("Alarm Received!");
    }

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalarm);
        alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        timePicker = (TimePicker)this.findViewById(R.id.alarmTimePicker);
        Button toggleButton = (Button)this.findViewById(R.id.button_toggleAlarm);
        toggleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(Application.TAG, "Toggled the alarm");
                setUpAlarm();
            }
        });
     //   timePicker.setOnClickListener(this);
    }

    private void setUpAlarm()
    {
        Intent intent = new Intent(SetAlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SetAlarmActivity.this, 0,intent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        Log.d(Application.TAG, "Set the alarm for h = " + timePicker.getCurrentHour() + " and second = " + timePicker.getCurrentMinute());
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000),
                pendingIntent);
    }


//    public void onToggleAlarmClick(View v)
//    {
//        Log.d(Application.TAG, "Clicked the toggle button");
//        switch(v.getId())
//        {
//            case R.id.button_toggleAlarm:
//                Log.d(Application.TAG, "Toggled alarm");
//                if(((ToggleButton)v).isChecked())
//                {
//                    Log.d(Application.TAG, "Turn on Alarm");
//                    setUpAlarm();
//                }
//                else
//                {
//                    alarmManager.cancel(pendingIntent);
//                    Log.d(Application.TAG, "Canceled the alarm");
//                    // Cancel the alarm.
//                }
//
//                break;
//        }
//    }
}

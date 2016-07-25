package apps.rahul.puzzlealarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Rahul on 7/24/2016.
 */
public class AlarmViewActivity extends AppCompatActivity {

    private ListView listView;
    private AlarmsCreatedListAdapter alarmsCreatedListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmview);
        listView = (ListView)this.findViewById(R.id.listView_alarmsSet);
        List<Alarm> alarmsSet = new ArrayList<Alarm>();
        alarmsSet.add(new Alarm(){{this.time_ms = 1;}});
        alarmsSet.add(new Alarm(){{this.time_ms = 2;}});
        alarmsSet.add(new Alarm(){{this.time_ms = 3;}});
        alarmsCreatedListAdapter = new AlarmsCreatedListAdapter(this, R.layout.activity_alarmview,alarmsSet);
        listView.setAdapter(alarmsCreatedListAdapter);

    }

}

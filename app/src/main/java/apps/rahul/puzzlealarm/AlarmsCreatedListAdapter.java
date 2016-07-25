package apps.rahul.puzzlealarm;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rahul on 7/24/2016.
 */
public class AlarmsCreatedListAdapter extends ArrayAdapter<Alarm> {

    private List<Alarm> alarmsSet;


    public AlarmsCreatedListAdapter(Context context, int textViewId)
    {
        super(context, textViewId);
    }

    public AlarmsCreatedListAdapter(Context context, int resourceId, List<Alarm> alarms)
    {
        super(context, resourceId, alarms);
        this.alarmsSet = alarms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View  v = convertView;
        if(v == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.layout_alarmset, null);
        }
        TextView  alarmTime  = (TextView)v.findViewById(R.id.alarmTime);
        alarmTime.setText("Position = " +alarmsSet.get(position).time_ms);
        return v;
    }
}

package clink.youparking;

import android.os.Bundle;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    final Calendar c = Calendar.getInstance();
    private long phpTime = 0;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        TextView tv = (TextView) getActivity().findViewById(R.id.timeText);

        Calendar calendar = Calendar.getInstance();
        calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                hourOfDay, minute, 0);
        System.out.println(c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
                + " " + hourOfDay + " " + minute);

        long time = calendar.getTimeInMillis();
        int hour = hourOfDay % 12;
        tv.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                minute, hourOfDay < 12 ? "AM" : "PM"));

        long diff = System.currentTimeMillis() - time;
        time += diff;
        phpTime = time/1000;
        User.time = phpTime;
        //System.out.println(time + " *@*@*@*@ " + System.currentTimeMillis());
    }

    public long getPhpTime() {
        return phpTime;
    }
}

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        TextView tv = (TextView) getActivity().findViewById(R.id.timeText);
        int hour = hourOfDay % 12;
        tv.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                minute, hourOfDay < 12 ? "AM" : "PM"));
    }
}

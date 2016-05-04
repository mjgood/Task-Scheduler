package com.example.razzaliaxindiferous.taskscheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Razzalia Xindiferous on 5/4/2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public String date = "";
    public String whereFrom;
    public DatePickerCallback callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month + 1;  //month is 0-based for some reason, but nothing else is

        date = year + "-";
        if (month < 10) { date = date + "0"; }
        date = date + month + "-";
        if (day < 10) { date = date + "0"; }
        date = date + day;

        callback.dateSet(date, whereFrom);
    }

    public static interface DatePickerCallback {
        abstract void dateSet(String formattedDate, String whereFrom);
    }
}
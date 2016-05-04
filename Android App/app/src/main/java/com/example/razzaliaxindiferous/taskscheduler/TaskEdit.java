//######################################################################
// Task Edit
//  Project: TaskScheduler
//  Authors:
//      Michael Good, 5/4/2016
//      Thomas Singleton, 5/4/2016
//      Josiah Hertzler, 5/4/2016
//######################################################################


package com.example.razzaliaxindiferous.taskscheduler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskEdit extends AppCompatActivity implements DatePickerFragment.DatePickerCallback {

    private int taskSelected = -1;
    private String completionStatus = "0";
    private boolean newTask = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        taskSelected = extras.getInt("itemSelected", 0);
        newTask = extras.getBoolean("newTask", false);

        // Populate the edit task fields
        String[] values = {
                "_id",
                "subject",
                "description",
                "priority",
                "completion_status",
                "completion_percentage",
                "start_time",
                "priority",
                "end_time"
        };
        String[] selectionArgs = {
                Long.toString(taskSelected)
        };
        Cursor c = getContentResolver().query(DailyTaskContentProvider.CONTENT_URI, values, "_id=?", selectionArgs, null);
        try {
            if (c.moveToFirst()) {
                if (!c.getString(1).equals("####")) { ((EditText) findViewById(R.id.editTaskName)).setText(c.getString(1));  }

                if (!c.getString(c.getColumnIndex("completion_percentage")).equals("")) { ((EditText) findViewById(R.id.editCompletion)).setText(c.getString(c.getColumnIndex("completion_percentage"))); }
                if (!c.getString(c.getColumnIndex("start_time")).equals("")) { ((EditText) findViewById(R.id.editTimeStart)).setText(c.getString(c.getColumnIndex("start_time"))); }
                if (!c.getString(c.getColumnIndex("end_time")).equals("")) { ((EditText) findViewById(R.id.editTimeEnd)).setText(c.getString(c.getColumnIndex("end_time"))); }
                if (!c.getString(c.getColumnIndex("priority")).equals("")) { ((EditText) findViewById(R.id.editPriority)).setText(c.getString(c.getColumnIndex("priority"))); }
                if (!c.getString(c.getColumnIndex("description")).equals("####")) { ((EditText) findViewById(R.id.editDescription)).setText(c.getString(c.getColumnIndex("description"))); }
                completionStatus = c.getString(c.getColumnIndex("completion_status"));
            }
        } catch (Exception e) {Log.d("Error:", "c.moveToFirst was null in TakEdit");}
        c.close();
    }

    //######################################################################

    //User swipes the screen to show the Navigation Panel
    public void swipeButton(View view)
    {
        Intent intent = new Intent(this, NavigationSidebar.class);
        startActivity(intent);
    }

    //User decides their changes are stupid
    public void cancelEdit(View view)
    {
        Intent intent = new Intent(this, DailyTaskList.class);
        startActivity(intent);
    }

    //User wants to save their changes to the task
    public void doneEdit(View view)
    {
        //check to ensure all variables are entered properly
        if (((EditText)findViewById(R.id.editTaskName)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter a Task Name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((EditText)findViewById(R.id.editCompletion)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter a number for Completion Percentage!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((EditText)findViewById(R.id.editTimeStart)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter a Start Date!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((EditText)findViewById(R.id.editTimeEnd)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter an End Date!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((EditText)findViewById(R.id.editPriority)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter a number for Priority!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((EditText)findViewById(R.id.editDescription)).getText().toString().equals("")) {
            Toast.makeText(this, "You must enter a Description!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store the input values in the database
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put("_id", taskSelected);
        values.put("subject", ((EditText)findViewById(R.id.editTaskName)).getText().toString());
        values.put("completion_percentage", Integer.parseInt(((EditText)findViewById(R.id.editCompletion)).getText().toString()));
        values.put("completion_status", "0");
        values.put("start_time", ((EditText)findViewById(R.id.editTimeStart)).getText().toString());
        values.put("end_time", ((EditText)findViewById(R.id.editTimeEnd)).getText().toString());
        values.put("priority", Integer.parseInt(((EditText)findViewById(R.id.editPriority)).getText().toString()));
        values.put("description", ((EditText)findViewById(R.id.editDescription)).getText().toString());

        if (newTask) {
            String taskEdit = (cr.insert(DailyTaskContentProvider.CONTENT_URI_NOLOCAL, values)).getLastPathSegment();
            cr.update(DailyTaskContentProvider.CONTENT_URI_NOREMOTE, values, "_id = " + taskSelected, null);
        } else {
            cr.update(DailyTaskContentProvider.CONTENT_URI, values, "_id = " + taskSelected, null);
        }

        //We should really just go back to the list after everything. Or make it do so if a task is deleted.
        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }

    public void pickStartDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).callback = this;
        ((DatePickerFragment) newFragment).whereFrom = "startDate";
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void pickEndDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).callback = this;
        ((DatePickerFragment) newFragment).whereFrom = "endDate";
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void dateSet(String formattedDate, String whereFrom) {
        if (whereFrom.equals("startDate")) {
            ((EditText) findViewById(R.id.editTimeStart)).setText(formattedDate);
        } else if (whereFrom.equals("endDate")) {
            ((EditText) findViewById(R.id.editTimeEnd)).setText(formattedDate);
        }
    }
}

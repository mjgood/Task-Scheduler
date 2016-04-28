//######################################################################
// Task Edit
//  Project: TaskScheduler
//  Author: Michael Good, 2/18/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskEdit extends AppCompatActivity {

    private int taskSelected = -1;
    private String completionStatus = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        taskSelected = extras.getInt("itemSelected", 0);

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
        if (c.moveToFirst()) {
            ((EditText) findViewById(R.id.editTaskName)).setText(c.getString(1));
            ((EditText) findViewById(R.id.editCompletion)).setText(c.getString(c.getColumnIndex("completion_percentage")));
            ((EditText) findViewById(R.id.editTimeStart)).setText(c.getString(c.getColumnIndex("start_time")));
            ((EditText) findViewById(R.id.editTimeEnd)).setText(c.getString(c.getColumnIndex("end_time")));
            ((EditText) findViewById(R.id.editPriority)).setText(c.getString(c.getColumnIndex("priority")));
            ((EditText) findViewById(R.id.editDescription)).setText(c.getString(c.getColumnIndex("description")));
            completionStatus = c.getString(c.getColumnIndex("completion_status"));
        }
        c.close();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        // Store the input values in the database
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put("_id", taskSelected);
        values.put("subject", ((EditText)findViewById(R.id.editTaskName)).getText().toString());
        //values.put("completion_status", Integer.parseInt(((EditText)findViewById(R.id.editTime)).getText().toString()));
        values.put("completion_percentage", Integer.parseInt(((EditText)findViewById(R.id.editCompletion)).getText().toString()));
        values.put("completion_status", "0");
        //values.put("repeat_id", (((CheckBox)findViewById(R.id.repeatCheckBox)).isChecked()) ? 1:0);
        values.put("start_time", ((EditText)findViewById(R.id.editTimeStart)).getText().toString());
        values.put("end_time", ((EditText)findViewById(R.id.editTimeEnd)).getText().toString());
        //values.put("deadline_time", ((EditText)findViewById(R.id.editTime)).getText().toString());
        //values.put("estimated_time", ((EditText)findViewById(R.id.editTime)).getText().toString());
        values.put("priority", Integer.parseInt(((EditText)findViewById(R.id.editPriority)).getText().toString()));
        //values.put("repeat_conditions", ((EditText)findViewById(R.id.???)).getText().toString());
        values.put("description", ((EditText)findViewById(R.id.editDescription)).getText().toString());
        values.put("completion_status", completionStatus);

        cr.update(DailyTaskContentProvider.CONTENT_URI, values, "_id = "+taskSelected, null);

        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }
}

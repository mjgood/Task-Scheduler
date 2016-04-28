//######################################################################
// Task View
//  Project: TaskScheduler
//  Author: Thomas Singleton, 2/18/2016
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskView extends AppCompatActivity {

    private int taskSelected = -1;

    private String[] queryResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        taskSelected = extras.getInt("itemSelected", 0);

        ContentResolver cr = getContentResolver();
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

        queryResults = new String[9];

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Log.d ("Task Selected:", Long.toString(taskSelected));
        String[] selectionArgs = {
                Long.toString(taskSelected)};

        Cursor c = cr.query(DailyTaskContentProvider.CONTENT_URI, values, "_id=?", selectionArgs, null);

        if (c.moveToFirst()) {
            ((TextView) findViewById(R.id.textViewSubject)).setText(c.getString(1));
            ((TextView) findViewById(R.id.textViewDescription)).setText(c.getString(2));
            ((TextView) findViewById(R.id.textViewPriority)).setText(c.getString(3));
            ((TextView) findViewById(R.id.textViewEndDate)).setText(c.getString(4));

            for (int ctr = 0; ctr < queryResults.length; ctr++) {
                queryResults[ctr] = c.getString(ctr);
            }
        }
    }

    //######################################################################

    //User swipes the screen to show the Navigation Panel
    public void swipeButton(View view)
    {
        Intent intent = new Intent(this, NavigationSidebar.class);
        startActivity(intent);
    }

    //User chooses to edit the task
    public void taskEdit(View view)
    {
        Intent intent = new Intent(this, TaskEdit.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }

    //User chooses to delete the task
    public void taskDelete(View view)
    {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id", taskSelected);
        values.put("subject", "<New Task>");
        values.put("description", "Description hardcoded at 12:56AM");

        String where = "_id = " + taskSelected;
        int deleteFlag = (cr.delete(DailyTaskContentProvider.CONTENT_URI, where, null));

        Intent intent = new Intent(this, DailyTaskList.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }

    //User chooses to complete the task
    public void taskComplete(View view)
    {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("id", taskSelected);
        values.put("subject", queryResults[1]);
        values.put("description", queryResults[2]);
        values.put("priority", queryResults[3]);
        values.put("completion_status", 1);
        values.put("completion_percentage", queryResults[5]);
        values.put("start_time", queryResults[6]);
        values.put("priority", queryResults[7]);
        values.put("end_time", queryResults[8]);

        String where = "_id = " + taskSelected;
        int deleteFlag = (cr.delete(DailyTaskContentProvider.CONTENT_URI, where, null));

        Intent intent = new Intent(this, DailyTaskList.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }
}

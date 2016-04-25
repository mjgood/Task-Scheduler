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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        taskSelected = extras.getInt("itemSelected", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ContentResolver cr = getContentResolver();
        String[] values = {
                "_id",
                "subject",
                "description",
                "priority",
                "end_time"};

        String[] results = new String[5];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Log.d ("Task Selected:", Long.toString(taskSelected));
        String[] selectionArgs = {
                Long.toString(taskSelected)};

        Cursor c = cr.query(DailyTaskContentProvider.CONTENT_URI, values, "_id=?", selectionArgs, null);

        if (c.moveToFirst()) {
            ((TextView) findViewById(R.id.textViewSubject)).setText(c.getString(1));
            ((TextView) findViewById(R.id.textViewDescription)).setText(c.getString(2));
            ((TextView) findViewById(R.id.textViewPriority)).setText(c.getString(3));
            ((TextView) findViewById(R.id.textViewEndDate)).setText(c.getString(4));
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
}

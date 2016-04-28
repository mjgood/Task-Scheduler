//######################################################################
// Task View
//  Project: TaskScheduler
//  Author: Thomas Singleton, 2/18/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
        ContentValues values = new ContentValues();
        values.get("_id");
        values.get("subject");
        values.get("description");
        values.get("priority");
        values.get("end_time");

        String[] results = new String[5];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(date);
        String dateFormatted = sdf.format(date.getTime());
        Log.d("Info", dateFormatted);

        values.put("deadline_time", dateFormatted);

        //cr.insert(DailyTaskContentProvider.CONTENT_URI, values);

        String selectionArgs = "_id=="+taskSelected+"";

        cr.query(DailyTaskContentProvider.CONTENT_URI, results, values, selectionArgs, null);
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

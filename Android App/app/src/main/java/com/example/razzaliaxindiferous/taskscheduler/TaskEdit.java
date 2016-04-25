//######################################################################
// Task Edit
//  Project: TaskScheduler
//  Author: Michael Good, 2/18/2016
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

public class TaskEdit extends AppCompatActivity {

    private int taskSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
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
    {ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("subject", "Dummy Subject2");
        values.put("description", "This is a dummy task created to hold things together");

        Calendar date = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        Log.d("Info", Integer.toString(date.get(Calendar.YEAR)));
        Log.d("Info", Integer.toString(date.get(Calendar.MONTH)));
        Log.d("Info", Integer.toString(date.get(Calendar.DAY_OF_MONTH)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(date);
        String dateFormatted = sdf.format(date.getTime());
        Log.d("Info", dateFormatted);

        values.put("deadline_time", dateFormatted);

        //cr.insert(DailyTaskContentProvider.CONTENT_URI, values);

        cr.update(DailyTaskContentProvider.CONTENT_URI, values, /* "_id=="+id */ null, null);

        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }
}

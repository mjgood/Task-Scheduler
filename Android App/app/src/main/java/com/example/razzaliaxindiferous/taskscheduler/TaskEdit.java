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
import android.widget.CheckBox;
import android.widget.EditText;

import java.sql.Date;
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

        /*
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT NOT NULL," +
                "completion_status TINYINT," +
                "completion_percentage TINYINT," +
                "repeat_id INTEGER," +
                "start_time DATETIME," +
                "end_time DATETIME," +
                "deadline_time DATETIME," +
                "estimated_time DATETIME," +
                "priority TINYINT," +
                "repeat_conditions TEXT," +
                "description TEXT)";
        */

        values.put("_id", taskSelected);
        values.put("subject", ((EditText)findViewById(R.id.editTaskName)).getText().toString());
        //values.put("completion_status", Integer.parseInt(((EditText)findViewById(R.id.editTime)).getText().toString()));
        values.put("completion_percentage", Integer.parseInt(((EditText)findViewById(R.id.editCompletion)).getText().toString()));
        //values.put("repeat_id", (((CheckBox)findViewById(R.id.repeatCheckBox)).isChecked()) ? 1:0);
        values.put("start_time", ((EditText)findViewById(R.id.editTimeStart)).getText().toString());
        values.put("end_time", ((EditText)findViewById(R.id.editTimeEnd)).getText().toString());
        //values.put("deadline_time", ((EditText)findViewById(R.id.editTime)).getText().toString());
        //values.put("estimated_time", ((EditText)findViewById(R.id.editTime)).getText().toString());
        values.put("priority", Integer.parseInt(((EditText)findViewById(R.id.editPriority)).getText().toString()));
        //values.put("repeat_conditions", ((EditText)findViewById(R.id.???)).getText().toString());
        values.put("description", ((EditText)findViewById(R.id.editDescription)).getText().toString());

        /*Calendar date = new GregorianCalendar(
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

        values.put("deadline_time", dateFormatted);*/

        //if(cr.query(DailyTaskContentProvider.CONTENT_URI,null,"_id="+taskSelected,null,null)!=null)
            cr.update(DailyTaskContentProvider.CONTENT_URI, values, "_id="+taskSelected, null);
        //else
            //cr.insert(DailyTaskContentProvider.CONTENT_URI, values);

        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("itemSelected", taskSelected);
        startActivity(intent);
    }
}

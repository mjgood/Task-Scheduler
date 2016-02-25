//######################################################################
// Daily Task List
//  Project: TaskScheduler
//  Author: Michael Good, 2/18/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.*;
import android.content.*;
import android.widget.*;

public class DailyTaskList extends AppCompatActivity {
    SQLiteDatabase TaskDB;
    long currentRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task_list);

        Database.getInstance(this).getWritableDatabase(new Database.OnDBReadyListener(){
            @Override
            public void onDBReady(SQLiteDatabase TaskDB){
                DailyTaskList.this.TaskDB=TaskDB;
            }
        });

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //########################################################################

    //User selects the Create Task button in title bar
    public void viewTask(View view)
    {
        Intent intent = new Intent(this, TaskView.class);
        startActivity(intent);
    }

    //User swipes the screen to show the Navigation Panel
    public void swipeButton(View view)
    {
        Intent intent = new Intent(this, NavigationSidebar.class);
        startActivity(intent);
    }
}

//######################################################################
// Navigation Sidebar
//  Project: TaskScheduler
//  Author: Michael Good, 2/18/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NavigationSidebar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_sidebar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    //User swipes the daily view
    public void viewDaily(View view)
    {
        Intent intent = new Intent(this, DailyTaskList.class);
        startActivity(intent);
    }

    //User selects the weekly view
    public void viewWeekly(View view)
    {
        Intent intent = new Intent(this, WeekView.class);
        startActivity(intent);
    }

    //User selects the monthly view
    public void viewMonthly(View view)
    {
        Intent intent = new Intent(this, MonthView.class);
        startActivity(intent);
    }

    //User swipes the settings view
    public void viewSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

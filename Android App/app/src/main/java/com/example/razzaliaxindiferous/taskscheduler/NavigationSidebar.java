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
import android.view.MenuInflater;
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
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}

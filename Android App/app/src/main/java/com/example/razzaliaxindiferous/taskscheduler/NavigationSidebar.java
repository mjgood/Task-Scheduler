//######################################################################
// Navigation Sidebar
//  Project: TaskScheduler
//  Authors:
//      Michael Good, 5/4/2016
//      Thomas Singleton, 5/4/2016
//      Josiah Hertzler, 5/4/2016
//######################################################################


package com.example.razzaliaxindiferous.taskscheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class NavigationSidebar extends AppCompatActivity implements
                                                RemoteServerAsyncTask.UpdateOnRemoteQueryFinished {

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

    //User forces a sync
    public void onSync(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // update the local server if it hasn't been updated recently
        try {
            if (getIntent().getExtras().getBoolean("loadedFromServer") != true) {
                RemoteServerAsyncTask rsat = new RemoteServerAsyncTask(getApplicationContext());
                rsat.setUpdateRemoteQuery(this);

                // TO-DO: Make server, port dynamic
                rsat.execute("query",
                        prefs.getString(getString(R.string.pref_rdb_uri), ""),
                        prefs.getString(getString(R.string.pref_rdb_port), ""));
            }
        } catch (NullPointerException e) {
            RemoteServerAsyncTask rsat = new RemoteServerAsyncTask(getApplicationContext());
            rsat.setUpdateRemoteQuery(this);
            rsat.execute("query",
                    prefs.getString(getString(R.string.pref_rdb_uri), ""),
                    prefs.getString(getString(R.string.pref_rdb_port), ""));
        }

        Intent intent = new Intent(this, DailyTaskList.class);
        finish();
        startActivity(intent);
    }

    //Sync is finished
    public void onRemoteQueryFinished() {
        Toast.makeText(this, "Database Synced!", Toast.LENGTH_SHORT).show();
    }
}

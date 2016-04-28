//######################################################################
// Daily Task List
//  Project: TaskScheduler
//  Author: Michael Good, 2/18/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.*;
import android.content.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import android.util.AttributeSet;
import android.net.Uri;

public class DailyTaskList extends AppCompatActivity implements
                                            LoaderManager.LoaderCallbacks<Cursor>,
                                            AdapterView.OnItemClickListener,
                                            AdapterView.OnItemLongClickListener,
                                            RemoteServerAsyncTask.UpdateOnRemoteQueryFinished {
    private SimpleCursorAdapter mAdapter;
    boolean filtered = false;

    SQLiteDatabase taskDB;
    long currentRow;
    Calendar dateDisplay;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_daily_task_list);

        //SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        dateDisplay = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        setContentView(R.layout.content_daily_task_list);
        if (savedInstanceState != null)
            filtered = savedInstanceState.getBoolean("filtered");

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_daily_task, null,
                new String[]{"_id", "subject", "description", "deadline_time"},
                new int[]{R.id.txtId, R.id.txtContent, R.id.txtDescription, R.id.txtDeadline}, 0);

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                return false;
            }
        });

        ListView listView = (ListView) findViewById(R.id.dailyTaskList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(1, null, this);

        // update the local server if it hasn't been updated recently
        try {
            if (getIntent().getExtras().getBoolean("loadedFromServer") != true) {
                RemoteServerAsyncTask rsat = new RemoteServerAsyncTask();
                rsat.setUpdateRemoteQuery(this);
                rsat.execute("query");
            } else {
                Toast.makeText(this, "Database Synced!", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            RemoteServerAsyncTask rsat = new RemoteServerAsyncTask();
            rsat.setUpdateRemoteQuery(this);
            rsat.execute("query");
        }

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("filtered", filtered);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Unimportant Toast test
        Toast.makeText(this, "Hello, "+prefs.getString(getString(R.string.pref_name), ""),
                Toast.LENGTH_LONG).show();
        //End Unimportant Toast Test
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = {"_id", "subject", "deadline_time", "description"};
            /*String[] columns = {"id", "subject", "completion_status", "completion_percentage",
                    "start_time", "end_time", "deadline_time",
                    "estimated_time", "priority", "description"}*/
        Log.d ("Where", Integer.toString(dateDisplay.get(Calendar.YEAR)));
        Log.d ("Where", Integer.toString(dateDisplay.get(Calendar.MONTH)));
        Log.d("Where", Integer.toString(dateDisplay.get(Calendar.DAY_OF_MONTH)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(dateDisplay);
        String dateFormatted = sdf.format(dateDisplay.getTime());

        String where = null;
        //String where = "deadline_time = Datetime(" + dateDisplay + ")";
                    /*Integer.toString(dateDisplay.get(Calendar.YEAR)) + "-"
                    + Integer.toString(dateDisplay.get(Calendar.MONTH)) + "-"
                    + Integer.toString(dateDisplay.get(Calendar.DAY_OF_MONTH)) + " 00:00:00')"
                + " and deadline_time <= Datetime('" + Integer.toString(dateDisplay.get(Calendar.YEAR)) + "-"
                + Integer.toString(dateDisplay.get(Calendar.MONTH)) + "-"
                + Integer.toString(dateDisplay.get(Calendar.DAY_OF_MONTH)) + " 23:59:59')";*/

        //INSERT REFERENCE TO CONTENT PROVIDER
        return new CursorLoader(this, DailyTaskContentProvider.CONTENT_URI,
                columns, where, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("Info:", cursor.toString());
        for (int ctr = 0; ctr < cursor.getColumnCount(); ctr++) {
            Log.d("Info:", cursor.getColumnName(ctr));
        }
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        int idSelected = Integer.parseInt(((TextView)view.findViewById(R.id.txtId)).getText().toString());

        Intent intent = new Intent(this, TaskView.class);
        intent.putExtra("itemSelected", idSelected);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                   long id) {
        Intent intent = new Intent(this, DailyTaskList.class);
        startActivity(intent);
        return true;
    }

    //########################################################################
    @Override
    // restart the activity when the remote query finishes updating the local database
    public void onRemoteQueryFinished() {
        finish();
        getIntent().putExtra("loadedFromServer", true);
        startActivity(getIntent());
    }

    //########################################################################

    //User selects the Pick Date button in title bar
    public void pickDate(MenuItem item) {
        //DialogFragment newFragment = new DatePickerFragment();
        //newFragment.show(getSupportFragmentManager (), "datePicker");
    }

    //User selects the Create Task button in title bar
    public void addTask(MenuItem item) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("subject", "<New Task>");

        String taskEdit = (cr.insert(DailyTaskContentProvider.CONTENT_URI, values)).getLastPathSegment();

        Intent intent = new Intent(this, TaskEdit.class);
        intent.putExtra("itemSelected", Integer.parseInt(taskEdit));
        startActivity(intent);
    }

    //User selects a task
    public void viewTask(View view) {
        Intent intent = new Intent(this, TaskView.class);
        startActivity(intent);
    }

    //User swipes the screen to show the Navigation Panel
    public void swipeButton(View view) {
        Intent intent = new Intent(this, NavigationSidebar.class);
        startActivity(intent);
    }
}
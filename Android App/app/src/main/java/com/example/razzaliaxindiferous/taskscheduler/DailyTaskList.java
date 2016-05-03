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
import android.text.method.TextKeyListener;
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
    String showDate;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_daily_task_list);

        //SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Set date being displayed to the current day
        dateDisplay = new GregorianCalendar(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setCalendar(dateDisplay);
        showDate = sdf.format(dateDisplay.getTime());
        ((EditText) findViewById(R.id.textDateDisplay)).setText(showDate);

        //Set content view
        setContentView(R.layout.content_daily_task_list);
        if (savedInstanceState != null)
            filtered = savedInstanceState.getBoolean("filtered");

        //Set the Cursor Adapter for the cursor item loader
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_daily_task, null,
                new String[]{"_id", "subject", "description", "completion_status",
                        "end_time", "completion_percentage"},
                new int[]{R.id.txtId,
                        R.id.txtContent,
                        R.id.txtDescription,
                        R.id.txtStatus,
                        R.id.txtEnd,
                        R.id.txtPercent}, 0);

        //colorize field if task is complete or overdue
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View newView, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex("completion_status")) {
                    if (cursor.getInt(columnIndex) == 1) {
                        try {
                            ((LinearLayout) newView.getRootView().getRootView()).setBackgroundColor(getResources().getColor(R.color.colorGrayGreen));
                            ((TextView) newView.findViewById(R.id.txtStatus)).setText("Done");
                        } catch (Exception e) {
                        }
                    }
                    return true;
                }
                if (columnIndex == cursor.getColumnIndex("end_time")) {
                    String[] splitResult = cursor.getString(columnIndex).split("-");
                    boolean overdue = false;
                    ((TextView) newView.findViewById(R.id.txtEnd)).setText(cursor.getString(columnIndex));

                    try {
                        if (!((TextView) newView.getRootView().getRootView().getRootView().findViewById(R.id.txtStatus)).getText().equals("Done")) {
                            if (dateDisplay.get(Calendar.YEAR) > Integer.parseInt(splitResult[0])) {
                                overdue = true;
                            } else if (dateDisplay.get(Calendar.YEAR) == Integer.parseInt(splitResult[0])) {
                                if (dateDisplay.get(Calendar.MONTH) + 1 > Integer.parseInt(splitResult[1])) {
                                    overdue = true;
                                } else if (dateDisplay.get(Calendar.MONTH) + 1 == Integer.parseInt(splitResult[1])) {
                                    if (dateDisplay.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(splitResult[2])) {
                                        overdue = true;
                                    }
                                }
                            }

                            if (overdue) {
                                ((LinearLayout) newView.getRootView().getRootView().getRootView()).setBackgroundColor(getResources().getColor(R.color.colorGrayRed));
                            }
                        }
                    } catch (Exception e) {
                    }
                    return true;
                }
                return false;
            }
        });

        //run content provider on ListView
        ListView listView = (ListView) findViewById(R.id.dailyTaskList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(1, null, this);

        // update the local server if it hasn't been updated recently
        if (prefs.getBoolean(getString(R.string.pref_rdb_category), false)) {
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
        }
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
        //Toast.makeText(this, "Hello, "+prefs.getString(getString(R.string.pref_name), ""),
        //        Toast.LENGTH_LONG).show();
        //End Unimportant Toast Test
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = {"_id", "subject", "start_time", "end_time", "description",
                "completion_status", "completion_percentage"};

        String dateFormatted = showDate + " 00:00:00";
        String where = "completion_status = 0 AND start_time <= DATETIME('" + dateFormatted + "')";

        //INSERT REFERENCE TO CONTENT PROVIDER
        return new CursorLoader(this, DailyTaskContentProvider.CONTENT_URI,
                columns, where, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
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
        Toast.makeText(this, "Database Synced!", Toast.LENGTH_SHORT).show();
        finish();
        getIntent().putExtra("loadedFromServer", true);
        startActivity(getIntent());
    }

    //########################################################################

    //User selects the Pick Date button in title bar
    public void pickDate(View view) {
        String showDate = ((EditText) view.getRootView().findViewById(R.id.textDateDisplay)).getText().toString();
        getLoaderManager().initLoader(1, null, this);
    }

    //User selects the Create Task button in title bar
    public void addTask(MenuItem item) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("subject", "");
        values.put("description", "");
        values.put("priority", "");
        values.put("completion_status", "");
        values.put("completion_percentage", "");
        values.put("start_time", "");
        values.put("end_time", "");

        String taskEdit = (cr.insert(DailyTaskContentProvider.CONTENT_URI_NOREMOTE, values)).getLastPathSegment();

        Intent intent = new Intent(this, TaskEdit.class);
        intent.putExtra("newTask", true);
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
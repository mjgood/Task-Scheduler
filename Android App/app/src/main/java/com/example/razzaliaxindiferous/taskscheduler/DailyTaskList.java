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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
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
                                            AdapterView.OnItemLongClickListener {
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
        setContentView(R.layout.activity_daily_task_list);

        setContentView(R.layout.content_daily_task_list);
        if (savedInstanceState != null)
            filtered = savedInstanceState.getBoolean("filtered");

        mAdapter = new SimpleCursorAdapter(this, R.layout.content_daily_task_list, null,
                new String[]{"_id", "subject", "description", "deadline_time"},
                new int[]{R.id.txtContent}, 0);

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                /*if (columnIndex == 2) {
                    switch (cursor.getString(cursor.getColumnIndex("liked"))) {
                        case "Y":
                            ((ImageView) view).setImageResource(
                                    R.drawable.ic_thumbs_up);
                            view.setTag("Y");
                            break;
                        case "N":
                            ((ImageView) view).setImageResource(
                                    R.drawable.ic_thumbs_down);
                            view.setTag("N");
                            break;
                        default:
                            ((ImageView) view).setImageResource(
                                    R.drawable.ic_question);
                            view.setTag("?");
                    }
                    final long rowid = cursor.getLong(cursor.getColumnIndex("_id"));
                    view.setOnClickListener(new View.OnClickListener() {
                        long _rowid = rowid;
                        public void onClick(View v) {
                            toggleImage(_rowid, (ImageView) v);
                        }
                    });
                    return true;
                }*/
                return false;
            }
        });

        ListView listView = (ListView) findViewById(R.id.dailyTaskList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(1, null, this);

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("filtered", filtered);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = null;

        dateDisplay = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

        String[] columns = {"_id", "subject", "deadline_time", "description"};
            /*String[] columns = {"id", "subject", "completion_status", "completion_percentage",
                    "start_time", "end_time", "deadline_time",
                    "estimated_time", "priority", "description"};*/
            /*String selection = "date(deadline_time) = "
                    + Integer.toString(dateDisplay.get(Calendar.YEAR)) + "-"
                    + Integer.toString(dateDisplay.get(Calendar.MONTH)) + "-"
                    + Integer.toString(dateDisplay.get(Calendar.DAY_OF_MONTH));*/

        String output;
        String toSay;

        //Cursor c = taskDB.query("tasks", columns, null, null, null, null, null);
        //Cursor c = taskDB.query("tasks", columns, selection, null, null, null, null);
        //Cursor c = dailyTaskDB.query("tasks",columns, null, null, null, null, orderBy);

        //INSERT REFERENCE TO CONTENT PROVIDER
        return new CursorLoader(this, DailyTaskContentProvider.CONTENT_URI,
                new String[]{"_id", "subject", "deadline_time", "description"}, where, null, null);
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
        Intent intent = new Intent(this, DailyTaskList.class);
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

    /*public void readDailyTasks()
    {
        if (taskDB==null){
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        }
        else {


            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    DailyTaskView viewToAdd = new DailyTaskView(this);
                    viewToAdd.setText(c.getString(0) + " : " +
                            c.getString(1) + " : " +
                            c.getString(2) + " : " +
                            c.getString(3));
                    //((ListView) findViewById(R.id.dailyTaskList)).addView(viewToAdd);
                }
            }
        }
    }*/

    //User selects the Pick Date button in title bar
    public void pickDate(MenuItem item) {
        //DialogFragment newFragment = new DatePickerFragment();
        //newFragment.show(getSupportFragmentManager (), "datePicker");
    }

    //User selects the Create Task button in title bar
    public void addTask(MenuItem item) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("subject", "Dummy Subject");
        values.put("description", "This is a dummy task created to hold things together");
        values.put("deadline_time", Integer.toString(dateDisplay.get(Calendar.YEAR)) + "-"
                + Integer.toString(dateDisplay.get(Calendar.MONTH)) + "-"
                + Integer.toString(dateDisplay.get(Calendar.DAY_OF_MONTH)));

        cr.insert(DailyTaskContentProvider.CONTENT_URI, values);
        finish();
    }

    //User selects the Create Task button in title bar
    public void viewTask(View view) {
        Intent intent = new Intent(this, TaskView.class);
        startActivity(intent);
    }

    //User swipes the screen to show the Navigation Panel
    public void swipeButton(View view) {
        Intent intent = new Intent(this, NavigationSidebar.class);
        startActivity(intent);
    }

    /*public static class DailyTaskView extends LinearLayout {
        private TextView tv;
        public DailyTaskView(Context context) {
            super(context);

            View.inflate(context, R.layout.daily_single_task, this);
            tv = (TextView) findViewById(R.id.testTextView);
        }

        public void setText(String text) {
            tv.setText(text);
        }
    }*/

    //########################################################################

    //Date Picker
    /*public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }*/
}
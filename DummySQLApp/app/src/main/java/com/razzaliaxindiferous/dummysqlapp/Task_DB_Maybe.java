package com.razzaliaxindiferous.dummysqlapp;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.AsyncTask;

/**
 * Created by Razzalia Xindiferous on 2/25/2016.
 */
public class Task_DB_Maybe extends SQLiteOpenHelper {

    // Constructor
    //######################################################################
    private Task_DB_Maybe(Context context) { //Only way to instantiate this class is through getInstance!
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Variables
    //######################################################################
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasks.db";
    private static Task_DB_Maybe theDb;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE tasks(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "SUBJECT TEXT NOT NULL," +
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

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS tasks";


    // Required Methods
    //######################################################################
    @Override public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_ENTRIES); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    // Database Access
    //######################################################################
    public interface OnDBReadyListener {
        public void onDBReady(SQLiteDatabase theDB);
    }

    public static synchronized Task_DB_Maybe getInstance(Context context) {
        if (theDb == null) { // Make sure that we do not leak Activity's context
            theDb = new Task_DB_Maybe(context.getApplicationContext());
        }

        return theDb;
    }

    public void getWritableDatabase(OnDBReadyListener listener) {
        new OpenDbAsyncTask().execute(listener);
    }

    private class OpenDbAsyncTask extends AsyncTask<OnDBReadyListener, Void, SQLiteDatabase> {
        OnDBReadyListener listener;

        @Override
        protected SQLiteDatabase doInBackground(OnDBReadyListener... params) {
            listener = params[0];
            return theDb.getWritableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
            listener.onDBReady(sqLiteDatabase);
        }
    }
}

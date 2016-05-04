//######################################################################
// Daily Task's Content Provider
//  Project: TaskScheduler
//  Authors:
//      Michael Good, 5/4/2016
//      Thomas Singleton, 5/4/2016
//      Josiah Hertzler, 5/4/2016
//######################################################################

package com.example.razzaliaxindiferous.taskscheduler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

public class DailyTaskContentProvider extends ContentProvider {
    private Database taskDB;

    private static final String AUTHORITY = "com.example.razzaliaxindiferous.taskscheduler";
    private static final String BASE_PATH = "tasks";
    private static final String NO_REMOTE = "tasks_no_remote";
    private static final String NO_LOCAL = "tasks_no_local";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
            BASE_PATH);
    public static final Uri CONTENT_URI_NOREMOTE = Uri.parse("content://" + AUTHORITY + "/" +
            NO_REMOTE);
    public static final Uri CONTENT_URI_NOLOCAL = Uri.parse("content://" + AUTHORITY + "/" +
            NO_LOCAL);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    private static final int TASKS_NO_REMOTE = 3;
    private static final int TASKS_NO_LOCAL = 4;
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASKS_ID);
        uriMatcher.addURI(AUTHORITY, NO_REMOTE, TASKS_NO_REMOTE);
        uriMatcher.addURI(AUTHORITY, NO_LOCAL, TASKS_NO_LOCAL);
    }

    String serverAddress = null;
    String port = null;

    @Override
    public boolean onCreate() {
        taskDB = Database.getInstance(getContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        serverAddress = prefs.getString(getContext().getString(R.string.pref_rdb_uri), "");
        port = prefs.getString(getContext().getString(R.string.pref_rdb_port), "");
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id = -1;
        boolean insertRemote = true;
        SQLiteDatabase db = taskDB.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case TASKS:
                id = db.insert("tasks", null, values);
                break;
            case TASKS_NO_REMOTE:
                id = db.insert("tasks", null, values);
                insertRemote = false;
                break;
            case TASKS_NO_LOCAL:
                Log.w("ID",Long.toString(id));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        if (insertRemote) {
            RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask(getContext());
            updateRemote.execute("insert",
                    serverAddress, port,
                    "id", Long.toString(id),
                    "subject", values.getAsString("subject"),
                    "description", values.getAsString("description"),
                    "priority", values.getAsString("priority"),
                    "completion_status", values.getAsString("completion_status"),
                    "completion_percentage", values.getAsString("completion_percentage"),
                    "start_time", values.getAsString("start_time"),
                    "end_time", values.getAsString("end_time"));
        }

        return Uri.parse(BASE_PATH + "/" + id);
    }



    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = taskDB.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case TASKS:
                cursor = db.query("tasks", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TASKS_ID:
                cursor = db.query("tasks", projection,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                Log.d("uri: ", CONTENT_URI.toString());
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        SQLiteDatabase db = taskDB.getWritableDatabase();
        boolean insertRemote = true;
        switch (uriMatcher.match(uri)){
            case TASKS:
                count = db.update("tasks", values, selection, selectionArgs);
                break;
            case TASKS_ID:
                count = db.update("tasks", values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case TASKS_NO_REMOTE:
                count = db.update("tasks", values, selection, selectionArgs);
                insertRemote = false;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        if (insertRemote) {
            RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask(getContext());
            updateRemote.execute("update",
                    serverAddress, port,
                    "id", values.getAsString("_id"),
                    "subject", values.getAsString("subject"),
                    "description", values.getAsString("description"),
                    "priority", values.getAsString("priority"),
                    "completion_status", values.getAsString("completion_status"),
                    "completion_percentage", values.getAsString("completion_percentage"),
                    "start_time", values.getAsString("start_time"),
                    "end_time", values.getAsString("end_time"));
        }

        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = taskDB.getWritableDatabase();
        boolean insertRemote = true;
        switch (uriMatcher.match(uri)){
            case TASKS:
                count = db.delete("tasks", selection, selectionArgs);
                break;
            case TASKS_ID:
                count = db.delete("tasks",
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            case TASKS_NO_REMOTE:
                count = db.delete("tasks", selection, selectionArgs);
                insertRemote = false;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        if (insertRemote) {
            RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask(getContext());
            String idToDelete = selection.substring(6, selection.length());
            updateRemote.execute("delete",
                    serverAddress, port,
                    "id", idToDelete);
        }

        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    private String appendIdToSelection(String selection, String sId) {
        int id = Integer.valueOf(sId);
        if (selection == null || selection.trim().equals(""))
            return "_ID = " + id;
        else
            return selection + " AND _ID = " + id;
    }
}

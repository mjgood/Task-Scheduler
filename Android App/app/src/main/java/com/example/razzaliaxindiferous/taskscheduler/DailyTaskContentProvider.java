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

public class DailyTaskContentProvider extends ContentProvider {
    private Database taskDB;

    private static final String AUTHORITY = "com.example.razzaliaxindiferous.taskscheduler";
    private static final String BASE_PATH = "tasks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" +
            BASE_PATH);

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TASKS = 1;
    private static final int TASKS_ID = 2;
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASKS_ID);
    }

    String serverAddress = null;
    String port = null;

    @Override
    public boolean onCreate() {
        taskDB = Database.getInstance(getContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String serverAddress = prefs.getString("RemoteDB_uri", "");
        String port = prefs.getString("RemoteDB_port", "");
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id = -1;
        SQLiteDatabase db = taskDB.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case TASKS:
                id = db.insert("tasks", null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask();
        updateRemote.execute("insert",
                serverAddress, port,
                "id", Long.toString(id),
                "subject", values.getAsString("subject"),
                "description", values.getAsString("description"));

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
        switch (uriMatcher.match(uri)){
            case TASKS:
                count = db.update("tasks", values, selection, selectionArgs);
                break;
            case TASKS_ID:
                count = db.update("tasks", values,
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask();
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

        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        SQLiteDatabase db = taskDB.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case TASKS:
                count = db.delete("tasks", selection, selectionArgs);
                break;
            case TASKS_ID:
                count = db.delete("tasks",
                        appendIdToSelection(selection, uri.getLastPathSegment()),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        RemoteServerAsyncTask updateRemote = new RemoteServerAsyncTask();
        String idToDelete = selection.substring(6, selection.length());
        updateRemote.execute("delete",
                serverAddress, port,
                "id", idToDelete);

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

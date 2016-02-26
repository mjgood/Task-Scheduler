//######################################################################
// Main Activity
//  Project: SQLite Demonstration
//  Author: Thomas Singleton, 2/25/2016
//######################################################################

package com.razzaliaxindiferous.dummysqlapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase dailyTaskDB;
    long currentRow;
    long last_id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TaskDB.getInstance(this).getWritableDatabase(new TaskDB.OnDBReadyListener() {
            @Override
            public void onDBReady(SQLiteDatabase taskDB) {
                // Will this.theDB work?
                MainActivity.this.dailyTaskDB = taskDB;
            }
        });

    }

    // C.REATE
    //######################################################################
    public void create(View v){
        if(dailyTaskDB==null){
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("subject","Example");
            values.put("completion_status", 0);
            values.put("description","This is a test description");

            last_id=dailyTaskDB.insert("tasks",null, values);

            if(last_id==0){
                Toast.makeText(this, "Must read table before updating", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Inserted Row " + last_id, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // R.EAD
    //######################################################################
    public void read(View v){
        if(dailyTaskDB==null){
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        }else{
            String[] columns = {"_id", "subject", "completion_status",
                                "completion_percentage", "repeat_id",
                                "start_time", "end_time", "deadline_time",
                                "estimated_time", "priority",
                                "repeat_conditions", "description"};
            String selection = "_id = ?";
            String[] selectionArgs={Long.toString(last_id)};

            String output;

            Cursor c = dailyTaskDB.query("tasks", columns, selection, selectionArgs, null, null, null);
            //Cursor c = dailyTaskDB.query("tasks",columns, null, null, null, null, orderBy);

            if(c.moveToFirst()){
                last_id = c.getInt(c.getColumnIndexOrThrow("_id"));
                output= "ID: "+c.getInt(c.getColumnIndexOrThrow("_id"))+"\n"+
                        "SUBJECT: "+c.getString(c.getColumnIndexOrThrow("subject"))+"\n"+
                        "COMPLETION STATUS: "+c.getString(c.getColumnIndexOrThrow("completion_status"))+"\n"+
                        "DESCRIPTION: "+c.getString(c.getColumnIndexOrThrow("description"));

                ((TextView) findViewById(R.id.interaction_text_view)).setText(output);
                Toast.makeText(this, "Read row "+last_id, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Could not find row", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // U.PDATE
    //######################################################################
    public void update(View v) {
        if (dailyTaskDB == null) {
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("subject", "Updated Example");
            values.put("completion_status", 1);
            values.put("description", "This is an updated test description");

            String selection = "_id=" + last_id;
            int rowCount=dailyTaskDB.update("tasks", values, selection, null);
            if(rowCount>0) {
                Toast.makeText(this, "Updated Row" + last_id, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No Rows to Update", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // D.ELETE
    //######################################################################
    public void delete(View v) {
        if (dailyTaskDB == null) {
            Toast.makeText(this, "Try again in a few seconds", Toast.LENGTH_SHORT).show();
        } else {
            String selection = "_id=(select max(_id) from tasks)";
            int rowCount = dailyTaskDB.delete("tasks", selection, null);

            if (rowCount > 0) {
                Toast.makeText(this, "Deleted Row", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No rows to delete", Toast.LENGTH_SHORT).show();
            }
            selection = "_id=(select max(_id) from tasks)";
            Cursor c = dailyTaskDB.query("tasks", null, selection, null, null, null, null);

            if (c.moveToFirst()) {
                last_id = c.getInt(c.getColumnIndexOrThrow("_id"));
            } else if (rowCount > 0){
                Toast.makeText(this, "Table is Empty", Toast.LENGTH_SHORT).show();
            last_id = 0;
            }
        }
        String text="(Click a button to interact with the database)";
        ((TextView) findViewById(R.id.interaction_text_view)).setText(text);
    }

}

package com.example.razzaliaxindiferous.taskscheduler;

import android.content.*;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Razzalia Xindiferous on 4/25/2016.
 */
//*******************************************************************************
//*     Async Task Server Functions
//*******************************************************************************

public class RemoteServerAsyncTask extends AsyncTask<String, Integer, Boolean> {
    String command = "";
    UpdateOnRemoteQueryFinished toUpdate = null;
    private Context rsContext = null;


    public RemoteServerAsyncTask(Context context){
        rsContext = context;
    }
    // Connect to server on AsyncTask thread
    //  Params:
    //      0 - Command
    //      1 - Server Address
    //      2 - Server Port
    //      3, 4 (and onward) - HTTP POST keys and values to send to the server. Exclude these for
    //          queries to the server.
    @Override
    protected Boolean doInBackground(String... params) {
        //vals
        command = params[0];
        String serverAddress = params[1];
        String serverPort = params[2];
        URL url = null;

        //Prep the string.
        String connectString = "http://";
        connectString = connectString.concat(serverAddress);
        //Only add the port and semi colon if necessary.
        if(serverPort!=null) {
            connectString = connectString.concat(":");
            connectString = connectString.concat(serverPort);
        }
        connectString = connectString.concat("/php/android/");
        HttpURLConnection urlConnection = null;


        String postParams="";

        //Can probably create a function for some of this.
        switch(command){
            case "insert":
                connectString = connectString.concat("insertTask.php");
                for(int c=3; c<params.length; c++){
                    if(c>3)
                        postParams+="&";
                    //If it's numeric, no quotes.
                    //If it's not numeric, use quotes.
                    if(isNumeric(params[c]))
                        postParams+=params[c]+"="+params[c+1];
                    else
                        postParams+=params[c]+"=\""+params[c+1]+"\"";
                    c++;
                }
                break;
            case "update":
                connectString = connectString.concat("updateTask.php");
                for(int c=3; c<params.length; c++){
                    if(c>3)
                        postParams+="&";
                    //If it's numeric, no quotes.
                    //If it's not numeric, use quotes.
                    if(isNumeric(params[c]))
                        postParams+=params[c]+"="+params[c+1];
                    else
                        postParams+=params[c]+"=\""+params[c+1]+"\"";
                    c++;
                }
                break;
            case "delete":
                connectString = connectString.concat("deleteTaskById.php");
                for(int c=3; c<params.length; c++){
                    if(c>3)
                        postParams+="&";
                    //If it's numeric, no quotes.
                    //If it's not numeric, use quotes.
                    if(isNumeric(params[c]))
                        postParams+=params[c]+"="+params[c+1];
                    else
                        postParams+=params[c]+"=\""+params[c+1]+"\"";
                    c++;
                }
                break;
            case "query":
                connectString = connectString.concat("getNewTaskList.php");
                break;
        }

        //URL connection attempt.
        try {
            url = new URL(connectString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        //Open the connection.
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Set our Request properties.
        urlConnection.setRequestProperty("User-Agent","Mozilla/5.0");
        urlConnection.setRequestProperty("Accept-Language", "en-US,en;0.5");
        urlConnection.setRequestProperty("Accept-Charset", "utf-8");
        urlConnection.setRequestProperty("Accept-Encoding", "identity");

        //We are doing output and input.
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        //Set our method to POST.
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        }

        //TIMEOUT for READ/CONNECT
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        //Our PrintWriter will be used for POSTING our output.
        PrintWriter out;
        try {
            out = new PrintWriter(urlConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //POST our params, and close.
        out.print(postParams);
        out.close();

        //Use a buffered reader to read the input.
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Use string builder to build our string. Should do the same for the previous string(s).
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while((line = br.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //Close our buffered reader.
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        //Should be a result. If actually a string, it's an error.
        //If null, no results.
        //If boolean, pass/failed.
        //If it gets parsed as a JSONArray, then we're successful.
        //We did it. :)
        String result = sb.toString();
        Log.w("RESULT",result);
        if(result.equalsIgnoreCase("true")){
            //Database interaction finished successfully
            return true;
        }else if(result.equalsIgnoreCase("false")){
            //Database interaction failed
            return false;
        }else if(result.equalsIgnoreCase("null")||result.equals("")){
            //no results
            return false;
        }else{
                try{
                    JSONArray jArray = new JSONArray(result);
                    for(int i=0; i<jArray.length(); i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        Log.d("RESPONSE",json_data.toString());
                        Database taskDB = Database.getInstance(rsContext);
                        SQLiteDatabase db = taskDB.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.put("subject", json_data.getString("subject"));
                        values.put("description", json_data.getString("description"));
                        try {
                            values.put("priority", json_data.getInt("priority"));
                        }catch(Exception e){
                            values.put("priority", 0);
                        }
                        try {
                            values.put("completion_status", json_data.getInt("completion_status"));
                        }catch(Exception e){
                            values.put("Completion_status",0);
                        }
                        try {
                            values.put("completion_percentage", json_data.getInt("completion_percentage"));
                        }catch(Exception e){
                            values.put("completion_percentage", 0);
                        }
                        try {
                            values.put("start_time", json_data.getString("start_time"));
                        }catch(Exception e){
                            values.put("start_time", "1999-12-25");
                        }
                        try {
                            values.put("end_time", json_data.getString("end_time"));
                        }catch(Exception e){
                            values.put("end_time", "1999-12-25");
                        }
                        long id= db.insert("tasks",null, values);
                        Log.w("id",Long.toString(id));
                        HttpURLConnection uConnect = null;
                        String conn = "http://"+serverAddress+":"+serverPort+"/php/android/deleteTaskById.php?new_task=1&id="+json_data.getInt("task_id");
                        URL url2 = null;
                        try {
                            url2=new URL(conn);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        try {
                            uConnect = (HttpURLConnection) url2.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        uConnect.setReadTimeout(10000);
                        uConnect.setConnectTimeout(15000);
                        try {
                            uConnect.setRequestMethod("GET");
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }

                        try {
                            uConnect.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            uConnect.getContent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        db.close();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
        }

        return true;
    }

    private boolean updateLocal(JSONObject json_data) {

        return false;
    }

    @Override
    protected void onPreExecute() { }

    @Override
    protected void onProgressUpdate(Integer... values) { }

    @Override
    protected void onCancelled() { }

    //call event for any listeners once server query is finished
    @Override
    protected void onPostExecute(Boolean result) {
        if (toUpdate != null && command.equals("query")) {
            toUpdate.onRemoteQueryFinished();
        }
    }

    public void setUpdateRemoteQuery(UpdateOnRemoteQueryFinished oTU) {
        toUpdate = oTU;
    }

    //interface and for creating event to execute when querying server is finished
    public static interface UpdateOnRemoteQueryFinished {
        abstract void onRemoteQueryFinished();
    }

    //http://developer.android.com/training/basics/network-ops/connecting.html
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public boolean isNumeric(String s){
        switch(s){
            case "id":
            case "completion_status":
            case "completion_percentage":
            case "repeat_id":
            case "priority":
                return true;
            default:
                return false;
        }
    }


}

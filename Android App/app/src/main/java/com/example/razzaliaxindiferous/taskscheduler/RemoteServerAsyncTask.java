package com.example.razzaliaxindiferous.taskscheduler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Razzalia Xindiferous on 4/25/2016.
 */
//*******************************************************************************
//*     Async Task Server Functions
//*******************************************************************************

public class RemoteServerAsyncTask extends AsyncTask<String, Integer, Boolean> {
    String command = "";
    UpdateOnRemoteQueryFinished toUpdate = null;

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
        HttpURLConnection urlConnection = null;
        String connectString = serverAddress;
            //connectString = connectString.concat(serverAddress);
            connectString = connectString.concat(":");
            connectString = connectString.concat(serverPort);
            connectString = connectString.concat("/php/android/");

        //create connection string
        switch (command) {
            case "insert":
                connectString = connectString.concat("insertTask.php?");
                for (int ctr = 3; ctr < params.length; ctr++) {
                    if (ctr > 3) { connectString = connectString.concat("&"); }
                    connectString = connectString.concat(params[ctr]);
                    connectString = connectString.concat("=");
                    ctr++;
                    connectString = connectString.concat(params[ctr]);
                }
                break;
            case "update":
                connectString = connectString.concat("updateTask.php?");
                for (int ctr = 3; ctr < params.length; ctr++) {
                    if (ctr > 3) { connectString = connectString.concat("&"); }
                    connectString = connectString.concat(params[ctr]);
                    connectString = connectString.concat("=");
                    ctr++;
                    connectString = connectString.concat(params[ctr]);
                }
                break;
            case "delete":
                connectString = connectString.concat("deleteTaskById.php?");
                connectString = connectString.concat(params[3]);
                connectString = connectString.concat("=");
                connectString = connectString.concat(params[4]);
                break;
            case "query":
                connectString = connectString.concat("getNewTaskList.php");
                break;
        }

        Log.d("Preformat URL: ", connectString);
        //format connect string to HTTP POST style
        connectString = connectString.replace("=", "=%27");
        connectString = connectString.replace("&", "%27&");
        connectString = connectString.replace(" ", "%20");
        connectString = connectString.replace("'", "");
        if (command != "query") { connectString = connectString.concat("%27"); }
        Log.d("Postformat URL: ", connectString);

        //create connection URL
        try { url = new URL(connectString); }
        catch (MalformedURLException e) { e.printStackTrace(); }
        try { urlConnection = (HttpURLConnection) url.openConnection(); }
        catch (IOException e) { e.printStackTrace(); }

        urlConnection.setReadTimeout(10000);        //timeout for read
        urlConnection.setConnectTimeout(15000);     //timeout for connect
        InputStream is = null;
        OutputStream os = null;
        int len = 500;              //length of returned data to read - may need to be increased?

        //execute call to server
        switch(command) {
            case "insert":
            case "update":
            case "delete":
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);

                    urlConnection.connect();
                    int response = urlConnection.getResponseCode();
                    Log.d("urlConnection: " + command, "The response is: " + response);
                    is = urlConnection.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = readIt(is, len);
                    Log.d("urlResults", contentAsString);
                } catch (IOException e) { e.printStackTrace(); }
                break;
            case "query":
                try { urlConnection.setRequestMethod("GET"); }
                catch (ProtocolException e) { e.printStackTrace(); }
                urlConnection.setDoInput(true);

                // HTTP GET call to server
                try {
                    urlConnection.connect();
                    int response = urlConnection.getResponseCode();
                    Log.d("urlConnection: query", "The response is: " + response);
                    is = urlConnection.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = readIt(is, len);
                    Log.d("urlResults", contentAsString);

                    // TO-DO: ADD RECORDS TO APPROPRIATE PLACES IN TABLES

                } catch (IOException e) { e.printStackTrace(); }
                break;
        }
        return true;
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
        if (toUpdate != null && command == "query") {
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
}

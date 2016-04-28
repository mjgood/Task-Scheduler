package com.example.razzaliaxindiferous.taskscheduler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
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

    @Override
    protected Boolean doInBackground(String... params) {
        command = params[0];
        //setup connection
        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            switch (command) {
                case "insert":
                    String connectString = "http://174.54.194.23:8010/php/android/insertTask.php?id=30&subject='Mobile Task'&description='This task came from Android!'";
                    connectString = connectString.replace("=", "=%27");
                    connectString = connectString.replace("&", "%27&");
                    connectString = connectString.replace(" ", "%20");
                    connectString = connectString.concat("%27");
                    connectString = connectString.replace("'", "");
                    url = new URL(connectString);
                    break;
                case "update":
                    url = new URL("http://174.54.194.23:8010/php/android/updateTask.php");
                    break;
                case "delete":
                    url = new URL("http://174.54.194.23:8010/php/android/deleteTaskById.php");
                    break;
                case "query":
                    url = new URL("http://174.54.194.23:8010/php/android/getNewTaskList.php");
                    break;
            }
        } catch (MalformedURLException e) { e.printStackTrace(); }


        try { urlConnection = (HttpURLConnection) url.openConnection(); }
        catch (IOException e) { e.printStackTrace(); }
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        InputStream is = null;
        OutputStream os = null;
        int len = 500;

        command = params[0];
        // http://174.54.194.23:8010/php/android/getNewTaskList.php

        switch(command) {
            case "insert":
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                }
                catch (ProtocolException e) { e.printStackTrace(); }
                urlConnection.setDoInput(true);

                // HTTP GET call to server
                try {
                    urlConnection.connect();
                    int response = urlConnection.getResponseCode();
                    Log.d("urlConnection: insert", "The response is: " + response);
                    is = urlConnection.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = readIt(is, len);
                    Log.d("urlResults", contentAsString);
                } catch (IOException e) { e.printStackTrace(); }
                break;
            case "update":
                // RPC call
                break;
            case "delete":
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
                } catch (IOException e) { e.printStackTrace(); }
                break;
        }

        return true;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onCancelled() {
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (toUpdate != null && command == "query") {
            toUpdate.onRemoteQueryFinished();
        }
    }

    public void setUpdateRemoteQuery(UpdateOnRemoteQueryFinished oTU) {
        toUpdate = oTU;
    }

    public static interface UpdateOnRemoteQueryFinished {
        abstract void onRemoteQueryFinished();
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}

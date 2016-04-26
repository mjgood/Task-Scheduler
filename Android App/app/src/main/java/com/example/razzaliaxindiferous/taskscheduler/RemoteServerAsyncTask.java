package com.example.razzaliaxindiferous.taskscheduler;

import android.os.AsyncTask;

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
        switch(command) {
            case "insert":
                // RPC call
                break;
            case "update":
                // RPC call
                break;
            case "delete":
                // RPC call
                break;
            case "query":
                // RPC call
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
}

package com.bloodlyne.firebase.function;

import android.os.AsyncTask;

import com.bloodlyne.asyncResponses.AsyncInviteResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class InviteResponseService extends AsyncTask<String, Void, String> {

    private URL url;
    private String jsonString;
    private AsyncInviteResponse asyncResponse;

    public InviteResponseService(URL url, String jsonString, AsyncInviteResponse asyncResponse) {
        this.url = url;
        this.jsonString = jsonString;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        String executionMessage = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(jsonString.getBytes());
            os.flush();

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream())); // If status is 200
            } catch (IOException e) {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //If status is not 200
            }

//        this.responseCode = conn.getResponseCode();
            executionMessage = bufferedReader.lines().collect(Collectors.joining()); // minSdkVersion version had to increased to use implementation
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executionMessage;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncResponse.onTaskCompleted(s);
    }
}

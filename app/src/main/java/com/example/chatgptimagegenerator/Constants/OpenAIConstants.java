package com.example.chatgptimagegenerator.Constants;

import android.os.AsyncTask;
import android.util.Log;

public class OpenAIConstants {
    public static final String BASE_URL = "https://api.openai.com/v1/images/generations";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_OPEN_AI = "Bearer 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";
    public static final String CANCEL_TAG = "cancel";


    public static void cancelAsyncTask(AsyncTask encodeAsyncTask) {
        AsyncTask.Status status;
        if (encodeAsyncTask != null) {
            status = encodeAsyncTask.getStatus();
            Log.i(CANCEL_TAG, "Status: " + status);
            if ((status == AsyncTask.Status.RUNNING) || (status == AsyncTask.Status.PENDING)) {

                Log.i(CANCEL_TAG, "The buton has pressed while the task is running");
                encodeAsyncTask.cancel(true);
                Log.i(CANCEL_TAG, "The task have just cancelled.");
            }
        } else Log.i(CANCEL_TAG, "Not started");

    }
}

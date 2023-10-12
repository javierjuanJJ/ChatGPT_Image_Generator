package com.example.chatgptimagegenerator.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatgptimagegenerator.Constants.OpenAIConstants;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AsyncTaskGenerateImage extends AsyncTask<String, Void, String>{
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final ImageView imgView;
    private final ProgressBar waitingBar;
    private final OkHttpClient client;
    private final MaterialButton funBtn;
    private final Context contextReference;
    private String imgUrl;

    public AsyncTaskGenerateImage(Context contextReference, ImageView imgView, ProgressBar waitingBar, OkHttpClient client, MaterialButton funBtn ) {
        this.imgView = new WeakReference<ImageView>(imgView).get();
        this.waitingBar = new WeakReference<ProgressBar>(waitingBar).get();
        this.client = new WeakReference<OkHttpClient>(client).get();
        this.funBtn = new WeakReference<MaterialButton>(funBtn).get();
        this.contextReference = new WeakReference<Context>(contextReference).get();
        imgUrl = "";

    }


    private void loadImage(String imgUrl) {
        Picasso.with(contextReference).load(imgUrl).into(imgView);
    }

    private void setWaiting(boolean b) {
        if(b){
            waitingBar.setVisibility(View.VISIBLE);
            funBtn.setVisibility(View.GONE);
        }
        else {
            waitingBar.setVisibility(View.GONE);
            funBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        setWaiting(true);
    }

    private String chatGPTApi(String ipt) {
        JSONObject json = new JSONObject();
        try {
            json.put("prompt",ipt);
            json.put("size","256x256");
        } catch (JSONException e) {

        }
        RequestBody requestBody = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder().url(OpenAIConstants.BASE_URL).header(OpenAIConstants.AUTHORIZATION, OpenAIConstants.TOKEN_OPEN_AI).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showToast("Failed to call ChatGPT");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.has("data")) {
                        imgUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                    }
                    else {
                        showToast("Error. The JSONObject variable not contains data name");
                    }
                } catch (JSONException e) {
                    // throw new RuntimeException(e);
                }
            }
        });
        return imgUrl;
    }


    @Override
    protected String doInBackground(String... strings) {
        return chatGPTApi(strings[0]);
    }
    private void showToast(String message){
        Looper.prepare();
        Toast.makeText(contextReference, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    protected void onPostExecute(String result) {
        if (!result.isEmpty()) {
            loadImage(result);
        }
        setWaiting(false);
    }
}

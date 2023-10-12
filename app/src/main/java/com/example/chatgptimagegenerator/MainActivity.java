package com.example.chatgptimagegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatgptimagegenerator.AsyncTasks.AsyncTaskGenerateImage;
import com.example.chatgptimagegenerator.Constants.OpenAIConstants;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private EditText inputTxt;
    private ImageView imgView;
    private ProgressBar waitingBar;
    private MaterialButton funBtn;
    private OkHttpClient client;

    private AsyncTask<String, Void, String> asyncTaskGenerateImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();
    }

    private void setUI() {
        client = new OkHttpClient();
        inputTxt = findViewById(R.id.input);
        funBtn = findViewById(R.id.fun_btn);
        waitingBar = findViewById(R.id.waiting_bar);
        imgView = findViewById(R.id.image_view);

        funBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipt = inputTxt.getText().toString().trim();
                if (ipt.isEmpty()) {
                    inputTxt.setText("Please enter text");
                }
                chatGPTApi(ipt);
            }
        });
    }

    private void chatGPTApi(String ipt) {
        OpenAIConstants.cancelAsyncTask(asyncTaskGenerateImage);
        asyncTaskGenerateImage = new AsyncTaskGenerateImage(getApplicationContext(), imgView, waitingBar, client, funBtn);
        asyncTaskGenerateImage.execute(ipt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenAIConstants.cancelAsyncTask(asyncTaskGenerateImage);
    }



}
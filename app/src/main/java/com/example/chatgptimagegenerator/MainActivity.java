package com.example.chatgptimagegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

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
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

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

        waitingBar.setOnClickListener(new View.OnClickListener() {
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
        setWaiting(true);
        JSONObject json = new JSONObject();
        try {
            json.put("prompt",ipt);
            json.put("size","256x256");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder().url("https://api.openai.com/v1/images/generations").header("Authorization","Bearer ").post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this, "Failed to call ChatGPT", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    private void setWaiting(boolean b) {

    }
}
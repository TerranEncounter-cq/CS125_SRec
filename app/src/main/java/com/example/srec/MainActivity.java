package com.example.srec;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button record;
    private Button stop;
    private Button delete;
    private Button search;

    private MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFile(mediaRecorder.setOutputFile(context.getFilesDir().getAbsolutePath() + "/mic.aac"));
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setAudioChannels(1);
//        mediaRecorder.setAudioSamplingRate(44100);
//        mediaRecorder.setAudioEncodingBitRate(192000);
        record = findViewById(R.id.record);
        search = findViewById(R.id.search);
        search.setVisibility(View.INVISIBLE);
        stop = findViewById(R.id.stop);
        stop.setVisibility(View.INVISIBLE);
        delete = findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        stop.setOnClickListener(unused -> stop());

        record.setOnClickListener(unused -> record());

        delete.setOnClickListener(unused -> delete());

        search.setOnClickListener(unused -> search());
        //search.setOnClickListener(unused -> sendApiAuthorization());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void onClickListener() {
        Button search = findViewById(R.id.search);
        search.setOnClickListener(v -> {
            onCreate();
        });
    }*/
    public void sendApiAuthorization () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ap-southeast-1.api.acrcloud.com/v1/identify";
        final TextView result = findViewById(R.id.result);
        final TextView A = findViewById(R.id.A);
        // Post information we've already have into the server.

        StringRequest auth = new StringRequest(Request.Method.POST, url,
                response -> {

                    // Display the first 500 characters of the response string.
                    try {
                        JSONObject toSend = new JSONObject();
                        toSend.put("email", "qic7@illinois.edu");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            A.setText(error.toString());
            result.setText("error auth|");
        }) {
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer WH9fcrNi3Octmj2QQA8rGU2FSXqbgWPk");
                return headers;
            }*/
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("access_key",String.valueOf("8b53c894de8426e743a93930d812b9aa"));
                return params;
            }

        };

// Add the request to the RequestQueue.
        queue.add(auth);
        // https://us-console.acrcloud.com/service/avr
    }
    public void record() {
        stop.setVisibility(View.VISIBLE);
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//
//        }
//        mediaRecorder.start();
    }
    public void stop() {
        stop.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
//        mediaRecorder.stop();
    }
    public void delete() {
        delete.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
    }
    public void search() {

    }
}

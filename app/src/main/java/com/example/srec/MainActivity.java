package com.example.srec;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;


public class MainActivity extends AppCompatActivity {
    /** four buttons of UI. */
    private Button record;
    private Button stop;
    private Button delete;
    private Button search;

    private TextView hint;
    private TextView a;
    private TextView result;
    private IdentifyProtocolV2 protocolV2;
    private String access_key;
    private String access_secret;
    private String host;
    private byte[] finalResultByteA;
    /** MediaRecorder for recording. */
    private MediaRecorder mediaRecorder;
    private File recorderFile;
    private byte[] resultByteA;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = findViewById(R.id.A);
        result = findViewById(R.id.result);
        hint = findViewById(R.id.content);
        i = 0;
        protocolV2 = new IdentifyProtocolV2();
        access_key = "f16580f1a90ba50fc8404e4b3b6c09c2";
        access_secret = "7fDCGZrza67GnIlIuxebhW86hN6lqd6gfo96PfXr";
        host = "identify-us-west-2.acrcloud.com";
        record = findViewById(R.id.record);
        record.setOnClickListener(unused -> {
            startRecording();
            record.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            hint.setText("Recording...");
        });

        stop = findViewById(R.id.stop);
        stop.setVisibility(View.GONE);
        stop.setOnClickListener(unused -> {
            stopRecording();
            if (recorderFile == null) {
                hint.setText("not found");
            } else {
                finalResultByteA = returnByte(recorderFile);
            }
            stop.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            hint.setText("Successfully recorded! \nPress <Search> to upload your record, or \nPress <Delete> to start over");
        });

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        finalResultByteA = resultByteA;
        search.setOnClickListener(unused -> {
            new DownloadFilesTask().execute();
            search.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            record.setVisibility(View.VISIBLE);
        }
        );

        delete = findViewById(R.id.delete);
        delete.setVisibility(View.GONE);
        delete.setOnClickListener(unused -> {
            deleteRecord();
            hint.setText("Start over.");
            record.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
        });
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... url) {
            String toReturn= protocolV2.recognize(host,
                    access_key,
                    access_secret, finalResultByteA, "audio", 10000);
            return toReturn;
        }
        protected void onPostExecute(String result) {
            getResult(result);
        }
    }
    private byte[] returnByte(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            hint.setText(e.toString());
        }

        byte[] result = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int read;

            while ((read = stream.read(temp)) >= 0) {
                buffer.write(temp, 0, read);
            }
            result = buffer.toByteArray();
            buffer.flush();
            stream.close();

        } catch (Exception e) {
            a.setText(e.toString());
        }
        System.out.println(result.toString());
        return result;
    }
    private void startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(this.getFilesDir().getAbsolutePath() + "/mic.aac");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setMaxDuration(15000);
        mediaRecorder.setMaxFileSize(120000);
        mediaRecorder.setAudioSamplingRate(16000);
        recorderFile = getRecorderFile();
        recorderFile.getParentFile().mkdirs();
        mediaRecorder.setOutputFile(recorderFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            hint.setText(e.toString());
        }
    }
    public void getResult(String info) {
        try {
            JSONObject obj = new JSONObject(info);
            String resultType = obj.getString("status");
            String date = obj.getJSONObject("metadata")
                    .getJSONArray("music")
                    .getJSONObject(0)
                    .getString("release_date");
            String playerName = obj.getJSONObject("metadata")
                    .getJSONArray("music")
                    .getJSONObject(0)
                    .getJSONArray("artists")
                    .getJSONObject(0).getString("name");
            String label = obj.getJSONObject("metadata")
                    .getJSONArray("music")
                    .getJSONObject(0)
                    .getString("label");
            String title = obj.getJSONObject("metadata")
                    .getJSONArray("music")
                    .getJSONObject(0)
                    .getString("title");
            String albumName = obj.getJSONObject("metadata")
                    .getJSONArray("music")
                    .getJSONObject(0)
                    .getJSONObject("album")
                    .getString("name");
            String overal = " Title: " + title + "\n" + " Artists Name" + playerName + "\n" + " Release Date: "
                    + date + "\n" + " Company Label: " + label + "\n" + " Album Name: " + albumName;
            hint.setText(overal);
        } catch(JSONException e) {
            hint.setText(e.toString());
        }
    }

    private File getRecorderFile() {
        i++;
        return new File(this.getFilesDir().getAbsolutePath(),
                "/Sound Recorder/RECORDING" + i + ".m4a");
    }
    private void stopRecording() {
        mediaRecorder.stop();
    }
    private void deleteRecord() {
        mediaRecorder = null;
    }
}

package com.example.srec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.GsonRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// import commons-codec-<version>.jar, download from http://commons.apache.org/proper/commons-codec/download_codec.cgi

public class MainActivity extends AppCompatActivity {
    /** four buttons of UI. */
    private Button record;
    private Button stop;
    private Button delete;
    private Button search;

    private TextView hint;
    private TextView a;
    private TextView result;
    private TextView exist;
    /** MediaRecorder for recording. */
    private MediaRecorder mediaRecorder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = findViewById(R.id.A);
        exist = findViewById(R.id.exist);
        result = findViewById(R.id.result);
        exist = findViewById(R.id.exist);

//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFile(mediaRecorder.setOutputFile(context.getFilesDir().getAbsolutePath() + "/mic.aac"));
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setAudioChannels(1);
//        mediaRecorder.setAudioSamplingRate(44100);

        byte[] result = null;
//        mediaRecorder.setAudioEncodingBitRate(192000);
        try {
            InputStream afd = getResources().openRawResource(R.raw.abc);
            result = returnByte(afd);
        } catch (Exception e) {
        }
        hint = findViewById(R.id.Hint);

        record = findViewById(R.id.record);
        record.setOnClickListener(unused -> {
            record.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            hint.setText("Recording...");

//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//
//        }
//        mediaRecorder.start();
        });


        search = findViewById(R.id.search);
        search.setVisibility(View.INVISIBLE);
        byte[] finalResult = result;
        search.setOnClickListener(unused -> sendApiAuthorization(finalResult));

        stop = findViewById(R.id.stop);
        stop.setVisibility(View.INVISIBLE);
        stop.setOnClickListener(unused -> {
            stop.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            hint.setText("Successfully recorded! \nPress <Search> to upload your record, or \nPress <Delete> to start over");
//        mediaRecorder.stop();
        });

        delete = findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        delete.setOnClickListener(unused -> {
            record.setVisibility(View.VISIBLE);
            delete.setVisibility(View.INVISIBLE);
            search.setVisibility(View.INVISIBLE);
        });
        //search.setOnClickListener(unused -> sendApiAuthorization());
    }
    private void sendApiAuthorization(byte[] data) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://identify-us-west-2.acrcloud.com/v1/identify";
        String BOUNDARYSTR = "*****2015.03.30.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****";
        String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
        String ENDBOUNDARY = "--" + BOUNDARYSTR + "--\r\n\r\n";
        Map<String,String> params = new HashMap<>();
        String http_method  = "Post";
        String http_uri = "/v1/identify";
        String timestamp = getUTCTimeSeconds();
        String access_key = "f16580f1a90ba50fc8404e4b3b6c09c2";
        String access_secret = "7fDCGZrza67GnIlIuxebhW86hN6lqd6gfo96PfXr";
        String data_type = "audio";
        String signature_version = "1";
        String string_to_sign = http_method + "\n"
                + http_uri + "\n"
                + access_key + "\n"
                + data_type + "\n"
                + signature_version + "\n"
                + timestamp;
        String signature = encryptByHMACSHA1(string_to_sign.getBytes(), access_secret.getBytes());
        params.put("access_key", access_key);
        params.put("data_type", "audio");
        params.put("sample_bytes", data.length + "");
        params.put("sample", new String(data));
        params.put("signature_version", signature_version);
        params.put("signature", signature);
        params.put("timestamp", timestamp);
        JSONObject param = new JSONObject(params);
        JsonObjectRequest auth = new JsonObjectRequest(Request.Method.POST, url, param,
                response -> {
                    // Display the first 500 characters of the response string.
                    hint.setText(response.toString());
                }, error -> {
            //a.setText(error.toString());
            result.setText(error.toString());
        }) {
            public String getBodyContentType()
            {
                return "multipart/form-data; boundary=" + BOUNDARYSTR + "; charset=utf-8";
            }
        };
        queue.add(auth);
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



    private String getUTCTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis()/1000 + "";
    }
    private String encodeBase64(byte[] bstr) {
        Base64 base64 = new Base64();
        return new String(base64.encode(bstr));
    }

    private String encryptByHMACSHA1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data);
            return encodeBase64(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private byte[] returnByte(InputStream file) {
        byte[] result = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int read;

            while((read = file.read(temp)) >= 0) {
                buffer.write(temp, 0, read);
            }
            result = buffer.toByteArray();
            buffer.flush();
            file.close();

        } catch (Exception e) {
            a.setText(e.toString());
        }
        System.out.println(result.toString());
        return result;
    }
}

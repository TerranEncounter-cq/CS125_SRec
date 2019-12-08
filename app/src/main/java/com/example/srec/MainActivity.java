package com.example.srec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    /** four buttons of UI. */
    private Button record;
    private Button stop;
    private Button delete;
    private Button search;

    private TextView hint;
    /** MediaRecorder for recording. */
    private MediaRecorder mediaRecorder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            InputStream afd = getResources().openRawResource(R.raw.bbbb);
            result = returnByte(afd);
        } catch (Exception e) {

        }
        hint = findViewById(R.id.Hint);
        TextView a = findViewById(R.id.A);
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
    public void sendApiAuthorization (final byte[] data) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://identify-us-west-2.acrcloud.com/v1/identify";
        final TextView result = findViewById(R.id.result);
        final TextView A = findViewById(R.id.A);

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
        params.put("access_key",access_key);
        params.put("data_type", "audio");
        params.put("sample_bytes", data.length + "");
        params.put("sample", new String(data));
        params.put("signature_version", signature_version);
        params.put("signature", signature);
        params.put("timestamp", timestamp);
        JSONObject toSend = new JSONObject(params);
        JsonObjectRequest auth = new JsonObjectRequest(Request.Method.POST, url,toSend,
                response -> {
                    hint.setText("successfully connected");
                    try {
                        JSONArray status = response.getJSONArray("status");
                        //JSONObject msg = status.getJSONObject("msg");
                        //String msgStr = msg.toString();
                        //hint.setText(msgStr);
                    } catch (Exception e) {
                        A.setText(e.toString());
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
                params.put("access_key",access_key);
                params.put("data_type", "audio");
                params.put("sample_bytes", data.length + "");
                params.put("sample", new String(data));
                params.put("signature_version", signature_version);
                params.put("signature", signature);
                params.put("timestamp", timestamp);
                return params;
            }

        };


// Add the request to the RequestQueue.
        queue.add(auth);
        // https://us-console.acrcloud.com/service/avr
        //ASADASDASDAS
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
        TextView A = findViewById(R.id.result);
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

            while((read = file.read(temp)) >= 0){
                buffer.write(temp, 0, read);
            }
            result = buffer.toByteArray();
            buffer.flush();
            file.close();

        } catch (Exception e) {
            A.setText(e.toString());
        }
        return result;
    }
}

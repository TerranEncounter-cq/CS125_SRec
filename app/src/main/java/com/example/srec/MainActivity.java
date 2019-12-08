package com.example.srec;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// import commons-codec-<version>.jar, download from http://commons.apache.org/proper/commons-codec/download_codec.cgi
import org.apache.commons.codec.binary.Base64;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
<<<<<<< HEAD
=======
import com.android.volley.toolbox.JsonArrayRequest;
>>>>>>> origin/master
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.codec.binary.Base64;
<<<<<<< HEAD
=======
import org.json.JSONArray;
>>>>>>> origin/master
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
            InputStream afd = getResources().openRawResource(R.raw.abc);
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
        //search.setOnClickListener(unused -> sendApiAuthorization(finalResult));

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

        String get = recognize("identify-us-west-2.acrcloud.com", "67a29ee156d0b704020ad60f076963cf",
                "OVIADsqufEwNnD8LZHqlNd0ycYUx1Sr6WnwitiwE", result, "audio", 10000);
        a.setText(get);

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

<<<<<<< HEAD
    private String postHttp(String posturl, Map<String, Object> params, int timeOut) {
        String res = "";
        String BOUNDARYSTR = "*****2015.03.30.acrcloud.rec.copyright." + System.currentTimeMillis() + "*****";
        String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
        String ENDBOUNDARY = "--" + BOUNDARYSTR + "--\r\n\r\n";

        String stringKeyHeader = BOUNDARY +
                "Content-Disposition: form-data; name=\"%s\"" +
                "\r\n\r\n%s\r\n";
        String filePartHeader = BOUNDARY +
                "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";

        URL url = null;
        HttpURLConnection conn = null;
        BufferedOutputStream out = null;
        BufferedReader reader = null;
        ByteArrayOutputStream postBufferStream = new ByteArrayOutputStream();
        try {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof String || value instanceof Integer) {
                    postBufferStream.write(String.format(stringKeyHeader, key, (String)value).getBytes());
                } else if (value instanceof byte[]) {
                    postBufferStream.write(String.format(filePartHeader, key, key).getBytes());
                    postBufferStream.write((byte[]) value);
                    postBufferStream.write("\r\n".getBytes());
                }
=======
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
>>>>>>> origin/master
            }
            postBufferStream.write(ENDBOUNDARY.getBytes());

            url = new URL(posturl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);

            conn.connect();
            out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postBufferStream.toByteArray());
            out.flush();
            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String tmpRes = "";
                while ((tmpRes = reader.readLine()) != null) {
                    if (tmpRes.length() > 0)
                        res = res + tmpRes;
                }
            }
        } catch (Exception e) {
            hint.setText(e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (postBufferStream != null) {
                    postBufferStream.close();
                    postBufferStream = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            } catch (IOException e) {
                hint.setText("Error found");
                e.printStackTrace();
            }
        }
        return res;

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

<<<<<<< HEAD
            while((read = file.read(temp)) >= 0) {
=======
            while((read = file.read(temp)) >= 0){
>>>>>>> origin/master
                buffer.write(temp, 0, read);
            }
            result = buffer.toByteArray();
            buffer.flush();
            file.close();

        } catch (Exception e) {
            A.setText(e.toString());
        }
        System.out.println(result.toString());
        return result;
    }

    /*protected Map<String,String> getParams(){
        Map<String,String> params = new HashMap<>();
        String http_method  = "Post";
        String http_uri = "/v1/identify";
        String timestamp = getUTCTimeSeconds();
        String access_key = "8b53c894de8426e743a93930d812b9aa";
        String data_type = "audio";
        String signature_version = "1";
        String string_to_sign = http_method + "\n"
                + http_uri + "\n"
                + access_key + "\n"
                + data_type + "\n"
                + signature_version + "\n"
                + timestamp;
        String signature = encryptByHMACSHA1(string_to_sign.getBytes(), "PdxqdTupBdTbGM25es9KzZwM0REiyeLZGBZJNOz7".getBytes());
        params.put("access_key","8b53c894de8426e743a93930d812b9aa");
        params.put("data_type", "audio");
        params.put("sample_bytes", .length + "");
        params.put("sample", new String(data));
        params.put("signature_version", signature_version);
        params.put("signature", signature);
        params.put("timestamp", timestamp);
        return params;
    }*/
    public String recognize(String host, String accessKey, String secretKey, byte[] queryData, String queryType, int timeout) {
        String method = "POST";
        String httpURL = "/v1/identify";
        String dataType = queryType;
        String sigVersion = "1";
        String timestamp = getUTCTimeSeconds();

        String reqURL = "http://" + host + httpURL;

        String sigStr = method + "\n" + httpURL + "\n" + accessKey + "\n" + dataType + "\n" + sigVersion + "\n" + timestamp;
        String signature = encryptByHMACSHA1(sigStr.getBytes(), secretKey.getBytes());

        Map<String, Object> postParams = new HashMap<String, Object>();
        postParams.put("access_key", accessKey);
        postParams.put("sample_bytes", queryData.length + "");
        postParams.put("sample", queryData);
        postParams.put("timestamp", timestamp);
        postParams.put("signature", signature);
        postParams.put("data_type", dataType);
        postParams.put("signature_version", sigVersion);

        String res = postHttp(reqURL, postParams, timeout);

        return res;
    }
}

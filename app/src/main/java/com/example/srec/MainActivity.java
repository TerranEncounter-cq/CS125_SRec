package com.example.srec;

import android.annotation.SuppressLint;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.apache.commons.codec.binary.Base64;
import java.util.HashMap;
import java.util.Map;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    /** four buttons of UI. */
    private Button record;
    private Button stop;
    private Button delete;
    private Button search;

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
//        mediaRecorder.setAudioEncodingBitRate(192000);
        TextView hint = findViewById(R.id.Hint);
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
        search.setOnClickListener(unused -> sendApiAuthorization());

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

    public String recognize(String host, String accessKey, String secretKey, byte[] queryData, String queryType, int timeout)
    {
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
        postParams.put("data_type", queryType);
        postParams.put("signature_version", sigVersion);

        String res = postHttp(reqURL, postParams, timeout);

        return res;
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
                e.printStackTrace();
            }
        }
        return res;
    }

    /*private String encryptByHMACSHA1(byte[] data, byte[] key) {
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
    }*/
    private String getUTCTimeSeconds() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTimeInMillis()/1000 + "";
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
        /*RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ap-southeast-1.api.acrcloud.com/v1/identify";
        final TextView result = findViewById(R.id.result);
        final TextView A = findViewById(R.id.A);
        // Post information we've already have into the server.

        StringRequest auth = new StringRequest(Request.Method.POST, url,
                response -> {
                }, error -> {
            A.setText(error.toString());
            result.setText("error auth|");
        }) {
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer WH9fcrNi3Octmj2QQA8rGU2FSXqbgWPk");
                return headers;
            }
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("access_key","8b53c894de8426e743a93930d812b9aa");
                return params;
            }

        };

// Add the request to the RequestQueue.
        queue.add(auth);*/
        // https://us-console.acrcloud.com/service/avr

    }
}

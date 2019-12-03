package com.example.srec;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.view.Display;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Context context = new Context() {
            @Override
            public AssetManager getAssets() {
                return null;
            }

            @Override
            public Resources getResources() {
                return null;
            }

            @Override
            public PackageManager getPackageManager() {
                return null;
            }

            @Override
            public ContentResolver getContentResolver() {
                return null;
            }

            @Override
            public Looper getMainLooper() {
                return null;
            }

            @Override
            public Context getApplicationContext() {
                return null;
            }

            @Override
            public void setTheme(int i) {

            }

            @Override
            public Resources.Theme getTheme() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public String getPackageName() {
                return null;
            }

            @Override
            public ApplicationInfo getApplicationInfo() {
                return null;
            }

            @Override
            public String getPackageResourcePath() {
                return null;
            }

            @Override
            public String getPackageCodePath() {
                return null;
            }

            @Override
            public SharedPreferences getSharedPreferences(String s, int i) {
                return null;
            }

            @Override
            public boolean moveSharedPreferencesFrom(Context context, String s) {
                return false;
            }

            @Override
            public boolean deleteSharedPreferences(String s) {
                return false;
            }

            @Override
            public FileInputStream openFileInput(String s) throws FileNotFoundException {
                return null;
            }

            @Override
            public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
                return null;
            }

            @Override
            public boolean deleteFile(String s) {
                return false;
            }

            @Override
            public File getFileStreamPath(String s) {
                return null;
            }

            @Override
            public File getDataDir() {
                return null;
            }

            @Override
            public File getFilesDir() {
                return null;
            }

            @Override
            public File getNoBackupFilesDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalFilesDir(@Nullable String s) {
                return null;
            }

            @Override
            public File[] getExternalFilesDirs(String s) {
                return new File[0];
            }

            @Override
            public File getObbDir() {
                return null;
            }

            @Override
            public File[] getObbDirs() {
                return new File[0];
            }

            @Override
            public File getCacheDir() {
                return null;
            }

            @Override
            public File getCodeCacheDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalCacheDir() {
                return null;
            }

            @Override
            public File[] getExternalCacheDirs() {
                return new File[0];
            }

            @Override
            public File[] getExternalMediaDirs() {
                return new File[0];
            }

            @Override
            public String[] fileList() {
                return new String[0];
            }

            @Override
            public File getDir(String s, int i) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, @Nullable DatabaseErrorHandler databaseErrorHandler) {
                return null;
            }

            @Override
            public boolean moveDatabaseFrom(Context context, String s) {
                return false;
            }

            @Override
            public boolean deleteDatabase(String s) {
                return false;
            }

            @Override
            public File getDatabasePath(String s) {
                return null;
            }

            @Override
            public String[] databaseList() {
                return new String[0];
            }

            @Override
            public Drawable getWallpaper() {
                return null;
            }

            @Override
            public Drawable peekWallpaper() {
                return null;
            }

            @Override
            public int getWallpaperDesiredMinimumWidth() {
                return 0;
            }

            @Override
            public int getWallpaperDesiredMinimumHeight() {
                return 0;
            }

            @Override
            public void setWallpaper(Bitmap bitmap) throws IOException {

            }

            @Override
            public void setWallpaper(InputStream inputStream) throws IOException {

            }

            @Override
            public void clearWallpaper() throws IOException {

            }

            @Override
            public void startActivity(Intent intent) {

            }

            @Override
            public void startActivity(Intent intent, @Nullable Bundle bundle) {

            }

            @Override
            public void startActivities(Intent[] intents) {

            }

            @Override
            public void startActivities(Intent[] intents, Bundle bundle) {

            }

            @Override
            public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

            }

            @Override
            public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2, @Nullable Bundle bundle) throws IntentSender.SendIntentException {

            }

            @Override
            public void sendBroadcast(Intent intent) {

            }

            @Override
            public void sendBroadcast(Intent intent, @Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, @Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String s, @Nullable BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

            }

            @Override
            public void sendStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

            }

            @Override
            public void removeStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @Override
            public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

            }

            @Override
            public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @Nullable
            @Override
            public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
                return null;
            }

            @Nullable
            @Override
            public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
                return null;
            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler) {
                return null;
            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler, int i) {
                return null;
            }

            @Override
            public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

            }

            @Nullable
            @Override
            public ComponentName startService(Intent intent) {
                return null;
            }

            @Nullable
            @Override
            public ComponentName startForegroundService(Intent intent) {
                return null;
            }

            @Override
            public boolean stopService(Intent intent) {
                return false;
            }

            @Override
            public boolean bindService(Intent intent, @NonNull ServiceConnection serviceConnection, int i) {
                return false;
            }

            @Override
            public void unbindService(@NonNull ServiceConnection serviceConnection) {

            }

            @Override
            public boolean startInstrumentation(@NonNull ComponentName componentName, @Nullable String s, @Nullable Bundle bundle) {
                return false;
            }

            @Override
            public Object getSystemService(@NonNull String s) {
                return null;
            }

            @Nullable
            @Override
            public String getSystemServiceName(@NonNull Class<?> aClass) {
                return null;
            }

            @Override
            public int checkPermission(@NonNull String s, int i, int i1) {
                return 0;
            }

            @Override
            public int checkCallingPermission(@NonNull String s) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfPermission(@NonNull String s) {
                return 0;
            }

            @Override
            public int checkSelfPermission(@NonNull String s) {
                return 0;
            }

            @Override
            public void enforcePermission(@NonNull String s, int i, int i1, @Nullable String s1) {

            }

            @Override
            public void enforceCallingPermission(@NonNull String s, @Nullable String s1) {

            }

            @Override
            public void enforceCallingOrSelfPermission(@NonNull String s, @Nullable String s1) {

            }

            @Override
            public void grantUriPermission(String s, Uri uri, int i) {

            }

            @Override
            public void revokeUriPermission(Uri uri, int i) {

            }

            @Override
            public void revokeUriPermission(String s, Uri uri, int i) {

            }

            @Override
            public int checkUriPermission(Uri uri, int i, int i1, int i2) {
                return 0;
            }

            @Override
            public int checkCallingUriPermission(Uri uri, int i) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfUriPermission(Uri uri, int i) {
                return 0;
            }

            @Override
            public int checkUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2) {
                return 0;
            }

            @Override
            public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

            }

            @Override
            public void enforceCallingUriPermission(Uri uri, int i, String s) {

            }

            @Override
            public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

            }

            @Override
            public void enforceUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2, @Nullable String s2) {

            }

            @Override
            public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createConfigurationContext(@NonNull Configuration configuration) {
                return null;
            }

            @Override
            public Context createDisplayContext(@NonNull Display display) {
                return null;
            }

            @Override
            public Context createDeviceProtectedStorageContext() {
                return null;
            }

            @Override
            public boolean isDeviceProtectedStorage() {
                return false;
            }
        }
        mediaRecorder.setOutputFile(mediaRecorder.setOutputFile(context.getFilesDir().getAbsolutePath() + "/mic.aac"));
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(192000);
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
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {

        }
        mediaRecorder.start();
    }
    public void stop() {
        stop.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        mediaRecorder.stop();
    }
    public void delete() {
        delete.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
    }
    public void search() {

    }
}

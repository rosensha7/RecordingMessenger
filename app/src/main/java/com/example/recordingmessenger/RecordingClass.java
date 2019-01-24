/*package com.example.audiorecorderexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnStopRecord, btnPlay, btnStop;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermissionFromDevice()){
            requestPermission();
        }

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setEnabled(false);
        btnRecord = (Button)findViewById(R.id.btnStartRecord);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);
        btnStopRecord.setEnabled(false);

        if(checkPermissionFromDevice()){
            //we can start recording
            btnRecord.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";

                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                    }

                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);
                    btnRecord.setEnabled(false);
                    btnStopRecord.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Recording has started.", Toast.LENGTH_SHORT).show();
                }
            });

            btnStopRecord.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    btnStopRecord.setEnabled(false);
                    btnPlay.setEnabled(true);
                    btnRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    btnRecord.setEnabled(false);
                    btnStop.setEnabled(true);
                    btnStopRecord.setEnabled(false);
                    btnPlay.setEnabled(false);

                    Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT);
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }

                    btnPlay.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnRecord.setEnabled(true);
                    btnStopRecord.setEnabled(false);
                }
            });
        }
        else {
            //request the permissions from the user
            requestPermission();
        }
    }

    public void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice(){
        //check that this app has permission to save onto external files
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //check that this app has permission to record audio
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        //if both permissions are granted, return true else false
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}*/
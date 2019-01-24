package com.example.recordingmessenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;
import android.util.DisplayMetrics;
import android.os.Handler;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button[][] btnMtx;
    MediaRecorder mediaRecorder;
    String pathSave = "";
    final int REQUEST_PERMISSION_CODE = 1000;

    int tapType = 0;
    boolean redBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout layout =  new TableLayout(this);

        getScreenDimension();

        if(!checkPermissionFromDevice())
            requestPermission();

        if(!checkPermissionFromDevice())
            requestPermission();
        else
            createBtnMatrix(layout);

        setContentView(layout);
    }

    private void getScreenDimension(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //Toast.makeText(MainActivity.this, "Width: " + dpWidth + " Height: " + dpHeight,Toast.LENGTH_LONG).show();
    }

    //generates button on the grid layout when app is created
    private void createBtnMatrix(TableLayout layout)
    {
        Integer x_matrix = Integer.parseInt(getString(R.string.x_matrix));
        Integer y_matrix = Integer.parseInt(getString(R.string.y_matrix));
        Integer btn_width = Integer.parseInt(getString(R.string.btn_width));
        Integer btn_height = Integer.parseInt(getString(R.string.btn_height));

        btnMtx = new Button[y_matrix][x_matrix];

        int k=1;
        for (int i = 0; i < y_matrix; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < x_matrix; j++) {
                btnMtx[i][j] = new Button(this);
                btnMtx[i][j].setText("Group "+k);
                k++;
                btnMtx[i][j].setId(k);
                btnMtx[i][j].setBackgroundResource(android.R.drawable.btn_default);

                //declare final indexes for the onClick loop to recognize the current button
                final int i_l = i;
                final int j_l = j;

                //set each button's onClickListener
                btnMtx[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tapType++;
                        if(redBackground) {
                            try {
                                stopRecordingAudio();
                            } catch (Exception e) {e.printStackTrace();}
                            redBackground=false;
                            tapType = 0;
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(tapType == 1){
                                    //single click
                                    Toast.makeText(MainActivity.this, "single click.",Toast.LENGTH_SHORT).show();
                                }
                                else if(tapType == 2){
                                    //double click
                                    Toast.makeText(MainActivity.this, "double tap.",Toast.LENGTH_SHORT).show();
                                    Intent playRecordsIntent = new Intent(getApplicationContext(), playRecordsActivity.class);
                                    startActivity(playRecordsIntent);
                                }
                                tapType = 0;
                            }
                        },1000);

                        for(int i=0;i<btnMtx.length;i++){
                            for(int j=0;j<btnMtx[0].length;j++){
                                if(i==i_l && j==j_l)
                                    btnMtx[i][j].setBackgroundResource(android.R.drawable.btn_default);//return button color to the default
                                else
                                    btnMtx[i][j].setEnabled(true);//enable the disabled buttons in the end of the recording
                            }
                        }
                    }
                });

                //set each button's onLongClickListener
                btnMtx[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        recordAudio();
                        Toast.makeText(MainActivity.this, "Recording...",Toast.LENGTH_SHORT).show();
                        for(int i=0;i<btnMtx.length;i++){
                            for(int j=0;j<btnMtx[i].length;j++){
                                if(i!=i_l || j!=j_l)
                                    btnMtx[i][j].setEnabled(false);
                            }
                        }
                        btnMtx[i_l][j_l].setBackgroundColor(Color.RED);
                        redBackground = true;
                        return false;//returning false means you activate onClickListener next
                    }
                });
                row.addView(btnMtx[i][j],btn_width ,btn_height);
            }
            layout.addView(row);
        }
    }

    private void recordAudio(){
        //get the path to where the file will be saved
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";//change to timestamp
        setupMediaRecorder();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch(IOException ioe){                    //note: make better catches!
            ioe.printStackTrace();
        }
    }

    private void stopRecordingAudio(){
        mediaRecorder.stop();
        mediaRecorder.release();
        Toast.makeText(MainActivity.this, "Recording finished.",Toast.LENGTH_SHORT).show();
    }

    public void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
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
}
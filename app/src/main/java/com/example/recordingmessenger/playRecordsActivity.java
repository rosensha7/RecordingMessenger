package com.example.recordingmessenger;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import java.io.File;
import java.util.UUID;

public class playRecordsActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    String pathSave = "";
    String items[] = new String[]{"Recording 1","Recording 2","Recording 3","Recording 4","Recording 5"};//array for testing
    int selectedBtnIndex = -1;
    String selectedFileName = "";
    //make dynamic for more than 5 files and scrollable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_records);

        final ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapterArray);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(playRecordsActivity.this, items[position], Toast.LENGTH_SHORT).show();
                listView.getChildAt(position).setBackgroundColor(
                        Color.parseColor("#00743D"));

                //shows the item you clicked on according to position
            }
        });

        final Button playRecordBtn = (Button) findViewById(R.id.playRecordBtn);

        Button stopRecordBtn = (Button) findViewById(R.id.stopRecordBtn);

        playRecordBtn.setOnClickListener(new View.OnClickListener(){//clicked a list item in order to play it.
            @Override
            public void onClick(View view){
                playRecordBtn.setEnabled(false);
                if(selectedBtnIndex != -1) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        //Toast.makeText(playRecordsActivity.this, "Playing "+items[position], Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                }
                playRecordBtn.setEnabled(true);
            }
        });

        stopRecordBtn.setOnClickListener(new View.OnClickListener(){//clicked a list item in order to play it.
            @Override
            public void onClick(View view){
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        Toast.makeText(playRecordsActivity.this, "Playing stopped.", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateListView(){
        //fills the list view with the file names as the appear inside the audio file directory
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath();
        Toast.makeText(playRecordsActivity.this, pathSave, Toast.LENGTH_SHORT).show();
        File directoryFiles = new File(pathSave);
        File[] files = directoryFiles.listFiles();
        items = new String[files.length];
        for(int i=0;i<files.length;i++){
            items[i] = files[i].getName();
        }
    }
}

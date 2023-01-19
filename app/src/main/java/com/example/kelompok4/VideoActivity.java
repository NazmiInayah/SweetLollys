package com.example.kelompok4;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {
    VideoView videoview;
    Button btnBack2;
    ListView listView;
    ArrayList<String> videolist;
    ArrayAdapter adapter;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoview = findViewById(R.id.videoview1);
        btnBack2 = findViewById(R.id.btnBack2);
        listView = findViewById(R.id.lvideo);
        videolist = new ArrayList<>();
        videolist.add("Cilukba");
        videolist.add("Finger Family");
        videolist.add("Lima Bayi Kecil");
        videolist.add("Jingle Bells");
        videolist.add("Wheels On The Bus");
        videolist.add("Kelap Kelip Bintang Kecil");
        videolist.add("Incy Wincy Spider");


        mediaController = new MediaController(this);
        videoview.setMediaController(mediaController);
        mediaController.setAnchorView(videoview);
        adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,videolist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cilukba));
                        break;
                    case 1:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.finger_family));
                        break;
                    case 2:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.lima_bayi_kecil));
                        break;
                    case 3:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.jingle_bells));
                        break;
                    case 4:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.wheels_on_the_bus));
                        break;
                    case 5:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.binar_binar_bintang_kecil));
                        break;
                    case 6:
                        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.incy_wincy_spider));
                        break;
                    default:
                        break;
                }
                videoview.requestFocus();
                videoview.start();
            }

        });


        btnBack2.setOnClickListener(view -> {
            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home);
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPause() {
        super.onPause();
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;
        int height = p.y;

        Rational rational = new Rational(width, height);

        PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
        builder.setAspectRatio(rational).build();
        enterPictureInPictureMode(builder.build());

    }
}
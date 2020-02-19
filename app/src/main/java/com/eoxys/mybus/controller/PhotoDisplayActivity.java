package com.eoxys.mybus.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eoxys.mybus.R;

import java.io.File;

public class PhotoDisplayActivity extends AppCompatActivity {

    String img_path;

    ImageView imgview;
    ImageButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        imgview = (ImageView) findViewById(R.id.imgdisplay);

        shareButton = (ImageButton) findViewById(R.id.shareButton);

        if(getIntent().hasExtra("Img_path")){

            img_path = getIntent().getStringExtra("Img_path");

            Glide.with(this).load(img_path).into(imgview);

        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(img_path)));
                startActivity(Intent.createChooser(share,"Share via"));
            }
        });
    }
}

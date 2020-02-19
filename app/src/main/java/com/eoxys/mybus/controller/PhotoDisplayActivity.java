package com.eoxys.mybus.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eoxys.mybus.R;

public class PhotoDisplayActivity extends AppCompatActivity {

    String img_path;

    ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);

        imgview = (ImageView) findViewById(R.id.imgdisplay);

        if(getIntent().hasExtra("Img_path")){

            img_path = getIntent().getStringExtra("Img_path");

            Glide.with(this).load(img_path).into(imgview);

        }
    }
}

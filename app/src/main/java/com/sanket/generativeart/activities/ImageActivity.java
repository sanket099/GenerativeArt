package com.sanket.generativeart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.sanket.generativeart.R;

import java.io.File;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().setTitle("");

        ImageView imageView = findViewById(R.id.img);

        String path = getIntent().getStringExtra("file");
        if(path != null && path.length() != 0){
            File file = new File(path);
            imageView.setImageURI(Uri.fromFile(file));
        }
    }
}
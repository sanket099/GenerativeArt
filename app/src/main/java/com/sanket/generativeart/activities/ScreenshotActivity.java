package com.sanket.generativeart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sanket.generativeart.adapters.FileAdapter;
import com.sanket.generativeart.fragments.BottomSheet;
import com.sanket.generativeart.util.LayoutManager;
import com.sanket.generativeart.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ScreenshotActivity extends AppCompatActivity implements FileAdapter.OnNoteList {

    ArrayList<File> files;
    File mediaStorage;
    RecyclerView fileRecyclerView;

    FileAdapter fileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().setTitle("Screenshots");

        files = new ArrayList<>();
        recyclerInit();

        mediaStorage = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));

        getData();

    }
    private void recyclerInit() {
        fileRecyclerView = findViewById(R.id.file_recycler);
        fileAdapter = new FileAdapter(getLayoutInflater(),files,this,this);
        LayoutManager gridLayoutManager = new LayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        fileRecyclerView.setLayoutManager(gridLayoutManager);

        fileRecyclerView.setAdapter(fileAdapter);
        fileRecyclerView.clearAnimation();


    }

    private void getData(){
        if(mediaStorage.listFiles() == null){
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }
        else {
            File[] filesArr = mediaStorage.listFiles();
            files.addAll(Arrays.asList(filesArr));
        }
        fileAdapter.notifyDataSetChanged();
    }


    @Override
    public void OnNoteClick(File userClass) {

        Intent intent = new Intent(ScreenshotActivity.this, ImageActivity.class);
        intent.putExtra("file", userClass.getPath());
        startActivity(intent);
        //finish();

    }


}
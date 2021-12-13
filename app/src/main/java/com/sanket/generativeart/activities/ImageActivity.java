package com.sanket.generativeart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.sanket.generativeart.R;

import java.io.File;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    String path ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);



        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().setTitle("");

        ImageView imageView = findViewById(R.id.img);

        path = getIntent().getStringExtra("file");
        if(path != null && path.length() != 0){
            File file = new File(path);
            imageView.setImageURI(Uri.fromFile(file));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.screenshot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.share){

            Uri imageUri = Uri.parse(path);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            //Target whatsapp:
            shareIntent.setPackage("com.whatsapp");
            //Add text and then Image URI
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Screenshot");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
            }



        }
        else if (item.getItemId() == R.id.delete){

            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage(
                        "Are you sure?")
                .setPositiveButton(
                        "Delete asap",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                File file = new File(path);
                                boolean deleted = file.delete();

                                if(deleted) {
                                    Toast.makeText(ImageActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ImageActivity.this, ScreenshotActivity.class);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            startActivity(intent);
                                        }
                                    }, 1000);


                                }
                                else {
                                    Toast.makeText(ImageActivity.this, "Couldnt do it :/",
                                            Toast.LENGTH_SHORT).show();
                                }



                            }
                        })
                .setNegativeButton(
                        ("No! Go Back"),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here


                            }
                        }).show();
    }}


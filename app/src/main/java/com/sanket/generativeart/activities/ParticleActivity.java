package com.sanket.generativeart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.widget.Toast;

import com.sanket.generativeart.fragments.BottomSheet;
import com.sanket.generativeart.adapters.ColorAdapter;
import com.sanket.generativeart.models.MyColor;
import com.sanket.generativeart.ParticleView;
import com.sanket.generativeart.R;
import com.sanket.generativeart.util.TinyDB;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ParticleActivity extends AppCompatActivity implements BottomSheet.BottomSheetListener, ColorAdapter.OnNoteList {

    private ParticleView contentView;
    File mediaStorage;
    //SharedPref sharedPref;
    TinyDB tinyDB;
    ArrayList<MyColor> selectedColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //sharedPref = new SharedPref(this);
        tinyDB = new TinyDB(ParticleActivity.this);
        contentView = new ParticleView(this);

        setContentView(contentView);
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().setTitle("");

        if(tinyDB.getListObject("colors", MyColor.class).size() == 0){
            MyColor red = new MyColor(0xFFFF0000, true);
            MyColor green = new MyColor(0xFF00FF00, true);
            MyColor yellow = new MyColor(0xFFFFFF00, true);

            ArrayList<MyColor> arrayList = new ArrayList<>();
            arrayList.add(red);
            arrayList.add(yellow);
            arrayList.add(green);

            tinyDB.putListObject("colors", arrayList);
            selectedColors = new ArrayList<>();

        }
        else{
            selectedColors = tinyDB.getListObject("colors", MyColor.class);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        contentView.dispatchTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.screenshot){

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                takePhoto();


            } else {
                // You can directly ask for the permission.
                requestPermissions(
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        1);
            }
            //takePhoto();


        }
        else if(item.getItemId() == R.id.settings){
            // customisations
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

        }
        else if(item.getItemId() == R.id.list){
            //show list of screenshots
            Intent intent = new Intent(ParticleActivity.this, ScreenshotActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePhoto() {

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(contentView.getWidth(), contentView.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        SurfaceView current = contentView;
        System.out.println(current.getHolder().getSurface().isValid());
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        PixelCopy.request(current, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
            @Override
            public void onPixelCopyFinished(int copyResult) {
                if(copyResult != PixelCopy.SUCCESS){
                    System.out.println("copyResult = " + copyResult);
                }
                else{
                    takeScreenshot(bitmap);
                    System.out.println("copyResult = " + copyResult);
                }
            }
        }, new Handler());
        handlerThread.quitSafely();
        new Handler(handlerThread.getLooper());


    }




    private void takeScreenshot(Bitmap b) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        mediaStorage = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        String link = mediaStorage
                + "/generative_art" + new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
                .format(new Date()) +
                ".jpg";

        System.out.println("link = " + link) ;

        try {

            // image naming and path  to include sd card  appending name you choose for file
            File imageFile = new File(link);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            b.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);

            Toast.makeText(this, "Screenshot taken " + link, Toast.LENGTH_SHORT).show();
            System.out.println("\"success\" = " + link);

           // addImageToGallery(mPath, this);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//        contentView = null;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        contentView = new ParticleView(this);
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        contentView = new ParticleView(this);
//    }

    @Override
    public void setSize(int size) {
        //sharedPref.addSize(size);
        tinyDB.putInt("size", size);
        contentView = new ParticleView(this);
        setContentView(contentView);


    }

    @Override
    public void setDensity(int density) {
        //sharedPref.addDensity(density);
        tinyDB.putInt("density", density);
        contentView = new ParticleView(this);
        setContentView(contentView);

    }

    @Override
    public void OnnoteClick(MyColor userClass) {
        userClass.setSelected(!userClass.isSelected());


        if(userClass.isSelected()){
            selectedColors.add(userClass);
        }
        else{
            selectedColors.remove(userClass);
        }

        tinyDB.putListObject("colors", selectedColors);

        contentView = new ParticleView(this);
        setContentView(contentView);
    }
}


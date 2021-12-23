package com.sanket.generativeart;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sanket.generativeart.models.MyColor;
import com.sanket.generativeart.models.Particle;
import com.sanket.generativeart.util.TinyDB;

class ParticleDrawingThread extends Thread {

    private boolean mRun = true;

    private final SurfaceHolder mSurfaceHolder;

    private final ArrayList<Particle> mParticleList =new ArrayList<>();
    private final ArrayList<Particle> mRecycleList =new ArrayList<>();

    private int mCanvasWidth;
    private int mCanvasHeight;
    private final Paint mPaint;
   // private final Bitmap[] mImage =new Bitmap[3];
    private final ArrayList<Bitmap> mImages = new ArrayList<>();
    //SharedPref sharedPref ;
    TinyDB tinyDB;

    public ParticleDrawingThread(SurfaceHolder mSurfaceHolder, Context mContext) {

        //sharedPref = new SharedPref(mContext);
        tinyDB = new TinyDB(mContext);

        this.mSurfaceHolder = mSurfaceHolder;
        this.mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        int density = tinyDB.getIntDensity("density");
        int size = tinyDB.getInt("size");



        ArrayList<MyColor> colorList = tinyDB.getListObject("colors", MyColor.class);


        while(density-- > 0){
            int idx = new Random().nextInt(colorList.size());
            System.out.println("colorList.size() = " + colorList.size());
            int color = colorList.get(idx).getColor();

            mImages.add(getBitmapFromVectorDrawable(mContext, R.drawable.red_circle, size, color));

        }

//        mImage[0] =getBitmapFromVectorDrawable(mContext, R.drawable.red_circle);
//        mImage[1] =getBitmapFromVectorDrawable(mContext, R.drawable.yellow_circle);
//        mImage[2] =getBitmapFromVectorDrawable(mContext, R.drawable.green_circle);


//        int density = sharedPref.getDensity();
//        int size = sharedPref.getSize();

//        int density = tinyDB.getIntDensity("density");
//        int size = tinyDB.getInt("size");
//        random = new Random().nextInt(density);
//
//        ArrayList<MyColor> colorList = tinyDB.getListObject("colors", MyColor.class);
//
//        while(density-- > 0){
//            mImages.add(createBitMap(size, colorList));
//        }

        //number of times = density

    }

    private Bitmap createBitMap(int size, ArrayList<MyColor> colorList) {

        System.out.println("\"bitmap create\" = " + "bitmap create");
        // Create a mutable bitmap
        Bitmap bitMap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bitMap = bitMap.copy(bitMap.getConfig(), true);
        // Construct a canvas with the specified bitmap to draw into
        Canvas canvas = new Canvas(bitMap);
        // Create a new paint with default settings.
        Paint paint = new Paint();
        // smooths out the edges of what is being drawn
        paint.setAntiAlias(true);
        // set color

        int colorIdx = new Random().nextInt(colorList.size());
        int color = colorList.get(colorIdx).getColor();
        System.out.println("colorIdx = " + colorIdx);
        paint.setColor(color);

        // set style
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // set stroke
        paint.setStrokeWidth(4.5f);
        // draw circle with radius 30
        canvas.drawCircle(50, 50, size, paint); //radius  = size
        // set on ImageView or any other view
        return bitMap;

    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int size, int color) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
//        drawable = new ScaleDrawable(drawable, 0, 10, 10).getDrawable();
//        drawable.setBounds(0, 0, 10, 10);
        DrawableCompat.setTint(
                DrawableCompat.wrap(drawable),
                color
        );

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, size, size);
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void run() {
        while (mRun) {
            Canvas c = null;

            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if (c!= null){
                        c.drawRect(0, 0, mCanvasWidth, mCanvasHeight, mPaint);
                        doDraw(c);
                    }


                }
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }

    }

    private void doDraw(Canvas c) {
        System.out.println("\"draw\" = " + "draw");

        int density = tinyDB.getIntDensity("density");


        synchronized (mParticleList) {
            int j = mImages.size() - 1;
            int count = 0;

            for (int i = 0; i< mParticleList.size() - 1; i++) {

                Particle p = (Particle) mParticleList.get(i);
                if(p!=null){

                    p.move();

                    System.out.println("j = " + j);

                    if(j<0) j = mImages.size() - 1;

                    c.drawBitmap(mImages.get(j--), p.x-10, p.y-10, mPaint);


                    System.out.println("mParticleList.size() = " + mParticleList.size());

                    if(tinyDB.getBoolean("blinking")){
                        if (p.x < 0 || p.x > mCanvasWidth || p.y < 0 || p.y > mCanvasHeight) {
                            mRecycleList.add(mParticleList.remove(i));
                            i--;
                        }
                    }

                }

            }
        }
    }


    public void stopDrawing() {
        this.mRun = false;
        this.interrupt();


    }

    public ArrayList<Particle> getParticleList() {
        return mParticleList;
    }

    public ArrayList<Particle> getRecycleList() {
        return mRecycleList;
    }

    public void setSurfaceSize(int width, int height) {
        mCanvasWidth = width;
        mCanvasHeight = height;
    }



}
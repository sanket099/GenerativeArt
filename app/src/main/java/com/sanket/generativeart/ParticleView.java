package com.sanket.generativeart;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sanket.generativeart.models.MyColor;
import com.sanket.generativeart.models.Particle;
import com.sanket.generativeart.util.TinyDB;

public class ParticleView extends SurfaceView implements SurfaceHolder.Callback {
    private ParticleDrawingThread mDrawingThread;

    private ArrayList<Particle> mParticleList;
    private ArrayList<Particle> mRecycleList;
    TinyDB tinyDB;

    private final Context mContext;

    public ParticleView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.mContext = context;
        tinyDB = new TinyDB(context);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mDrawingThread.setSurfaceSize(width, height);
        System.out.println("\"changed\" = " + "changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawingThread = new ParticleDrawingThread(holder, mContext);
        mParticleList = mDrawingThread.getParticleList();
        mRecycleList = mDrawingThread.getRecycleList();
        mDrawingThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mDrawingThread.stopDrawing();
        while (retry) {
            try {
                mDrawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Particle p;
        int recycleCount = 0;
        ArrayList<MyColor> colorList = tinyDB.getListObject("colors", MyColor.class);

        if(mRecycleList.size()>1)
            recycleCount = 2;
        else
            recycleCount =mRecycleList.size();

        int colorIdx = new Random().nextInt(colorList.size());
        int color = colorList.get(colorIdx).getColor();


        for (int i = 0; i < recycleCount; i++) {
            p = (Particle) mRecycleList.remove(0);
            p.init((int) event.getX(), (int) event.getY(), color);

            mParticleList.add(p);

        }


        for (int i = 0; i < 2-recycleCount; i++){

            mParticleList.add(new Particle((int)event.getX(), (int)event.getY(), color));
        }

        System.out.println("\"touch\" = " + "touch");

        return super.onTouchEvent(event);
    }

}

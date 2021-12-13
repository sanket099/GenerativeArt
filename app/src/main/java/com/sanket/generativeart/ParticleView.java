package com.sanket.generativeart;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
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


    // test
//    public static final int NB = 14;
//    private Paint black, yellow, white;
//    private ArrayList<Key> whites = new ArrayList<>();
//    private ArrayList<Key> blacks = new ArrayList<>();
//    private int keyWidth, height;
//    private AudioSoundPlayer soundPlayer;

    public ParticleView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.mContext = context;
        tinyDB = new TinyDB(context);

        //test
//        black = new Paint();
//        black.setColor(Color.BLACK);
//        white = new Paint();
//        white.setColor(Color.WHITE);
//        white.setStyle(Paint.Style.FILL);
//        yellow = new Paint();
//        yellow.setColor(Color.YELLOW);
//        yellow.setStyle(Paint.Style.FILL);
//        soundPlayer = new AudioSoundPlayer(context);

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
        if(mRecycleList != null && mParticleList != null){

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

        }

        //test

        /*int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            Key k = keyForCoords(x,y);

            if (k != null) {
                k.down = isDownAction;
            }
        }

        ArrayList<Key> tmp = new ArrayList<>(whites);
        tmp.addAll(blacks);

        for (Key k : tmp) {
            if (k.down) {
                if (!soundPlayer.isNotePlaying(k.sound)) {
                    soundPlayer.playNote(k.sound);
                    invalidate();
                } else {
                    releaseKey(k);
                }
            } else {
                soundPlayer.stopNote(k.sound);
                releaseKey(k);
            }
        }
*/
        return super.onTouchEvent(event);
    }

    /*private Key keyForCoords(float x, float y) {
        for (Key k : blacks) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        for (Key k : whites) {
            if (k.rect.contains(x,y)) {
                return k;
            }
        }

        return null;
    }

    private void releaseKey(final Key k) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k.down = false;
                handler.sendEmptyMessage(0);
            }
        }, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
*/
}

package com.example.flybird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;




public class BaseView extends SurfaceView implements Callback, Runnable {
    protected float scaleX;
    protected float scaleY;
    protected MainActivity mainActivity;
    protected SoundPlayer soundPlayer;
    protected Canvas canvas;
    protected Paint paint;
    protected SurfaceHolder sfh;
    protected Thread thread;
    protected boolean threadFlag;

    public BaseView(Context context, SoundPlayer soundPlayer) {
        super(context);
        this.mainActivity = (MainActivity)context;
        this.soundPlayer = soundPlayer;
        this.sfh = this.getHolder();
        this.sfh.addCallback(this);
        this.paint = new Paint();
    }

    public void run() {
    }

    public void initBitmap() {
    }

    public void release() {
    }

    public void drawSelf() {
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Constants.SCREEN_WIDTH = (float)this.getWidth();
        Constants.SCREEN_HEIGHT = (float)this.getHeight();
        this.threadFlag = true;
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        this.threadFlag = false;
    }

    public void setThreadFlag(boolean threadFlag) {
        this.threadFlag = threadFlag;
    }
}


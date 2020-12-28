package com.example.flybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.SurfaceHolder;




public class LoadingView extends BaseView {
    private Bitmap bgImg;
    private Bitmap logoImg;
    private Bitmap textImg;
    private float logoImgX;
    private float logoImgY;
    private float textImgX;
    private float textImgY;
    private String author = "仿写者：陈孝泓";
    private Rect rect = new Rect();
    private float strWidth;
    private float strHeight;
    private float textX;
    private float textY;

    public LoadingView(Context context, SoundPlayer soundPlayer) {
        super(context, soundPlayer);
        this.thread = new Thread(this);
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        super.surfaceCreated(arg0);
        this.initBitmap();
        if (this.thread.isAlive()) {
            this.thread.start();
        } else {
            this.thread = new Thread(this);
            this.thread.start();
        }

    }

    public void initBitmap() {
        this.bgImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
        this.logoImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
        this.textImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.text_logo);
        this.scaleX = Constants.SCREEN_WIDTH / (float)this.bgImg.getWidth();
        this.scaleY = Constants.SCREEN_HEIGHT / (float)this.bgImg.getHeight();
        this.textImgX = (Constants.SCREEN_WIDTH - (float)this.textImg.getWidth()) / 2.0F;
        this.textImgY = Constants.SCREEN_HEIGHT / 2.0F - (float)(this.textImg.getHeight() * 2);
        this.logoImgX = (Constants.SCREEN_WIDTH - (float)this.logoImg.getWidth()) / 2.0F;
        this.logoImgY = Constants.SCREEN_HEIGHT / 2.0F - (float)(this.logoImg.getWidth() * 0);
        this.paint.setTextSize(40.0F);
        this.paint.getTextBounds(this.author, 0, this.author.length(), this.rect);
        this.strWidth = (float)this.rect.width();
        this.strHeight = (float)this.rect.height();
        this.textX = Constants.SCREEN_WIDTH / 2.0F - this.strWidth / 2.0F;
        this.textY = Constants.SCREEN_HEIGHT / 2.0F + (float)this.logoImg.getHeight() + this.strHeight * 2.0F;
    }

    public void run() {
        for(; this.threadFlag; this.threadFlag = false) {
            this.drawSelf();

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

        this.mainActivity.getHandler().sendEmptyMessage(1);
    }

    public void drawSelf() {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.canvas.save();
            this.canvas.scale(this.scaleX, this.scaleY);
            this.canvas.drawBitmap(this.bgImg, 0.0F, 0.0F, this.paint);
            this.canvas.restore();
            this.canvas.drawBitmap(this.textImg, this.textImgX, this.textImgY, this.paint);
            this.canvas.drawBitmap(this.logoImg, this.logoImgX, this.logoImgY, this.paint);
            this.canvas.drawText(this.author, this.textX, this.textY, this.paint);
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        super.surfaceDestroyed(arg0);
        this.release();
    }

    public void release() {
        if (!this.bgImg.isRecycled()) {
            this.bgImg.recycle();
        }

        if (!this.logoImg.isRecycled()) {
            this.logoImg.recycle();
        }

        if (!this.textImg.isRecycled()) {
            this.textImg.recycle();
        }

    }
}

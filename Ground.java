package com.example.flybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;




public class Ground extends GameObject {
    private Bitmap groundImg;
    private Rect obj_rect = new Rect();

    public Ground(Resources resources) {
        super(resources);
        this.obj_x = 0.0F;
        this.initBitmap();
    }

    public void step() {
        this.obj_x -= (float) Config.SPEED;
        if (this.obj_x <= -(this.obj_width - Constants.SCREEN_WIDTH)) {
            this.obj_x = -15.0F;
        }

    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(this.groundImg, this.obj_x, this.obj_y, this.paint);
    }

    public void initBitmap() {
        this.groundImg = BitmapFactory.decodeResource(this.resources, R.drawable.ground);
        this.obj_width = (float)this.groundImg.getWidth();
        this.obj_height = (float)this.groundImg.getHeight();
        this.obj_y = Constants.SCREEN_HEIGHT - this.obj_height;
    }

    public Rect getObjRect() {
        this.obj_rect.set(0, (int)this.obj_y, (int)Constants.SCREEN_WIDTH, (int)Constants.SCREEN_HEIGHT);
        return this.obj_rect;
    }

    public void release() {
        if (!this.groundImg.isRecycled()) {
            this.groundImg.recycle();
        }

    }
}

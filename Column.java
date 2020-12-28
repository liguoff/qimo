package com.example.flybird;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;



import java.util.Random;


public class Column extends GameObject {
    private Bitmap columnImg;
    private Random random;
    private float groundHeight;
    private Rect rectTop = new Rect();
    private Rect rectBottom = new Rect();

    public Column(Resources resources, float x, float groundHeight) {
        super(resources);
        this.groundHeight = groundHeight;
        this.random = new Random();
        this.obj_mid_x = x;
        this.obj_mid_y = (float)this.random.nextInt((int)(Constants.SCREEN_HEIGHT - this.groundHeight - Config.COLUMN_Y_GAP * 2.0F)) + Config.COLUMN_Y_GAP;
        this.initBitmap();
    }

    public void step() {
        this.obj_mid_x -= (float) Config.SPEED;
        if (this.obj_mid_x <= -(Config.COLUMN_X_GAP - this.obj_width / 2.0F)) {
            this.obj_mid_x = Config.COLUMN_X_GAP * 2.0F + this.obj_width / 2.0F;
            this.obj_mid_y = (float)this.random.nextInt((int)(Constants.SCREEN_HEIGHT - this.groundHeight - Config.COLUMN_Y_GAP * 2.0F)) + Config.COLUMN_Y_GAP;
        }

    }

    public void drawSelf(Canvas canvas) {
        canvas.drawBitmap(this.columnImg, this.obj_mid_x - this.obj_width / 2.0F, this.obj_mid_y - this.obj_height / 2.0F, this.paint);
    }

    public void initBitmap() {
        this.columnImg = BitmapFactory.decodeResource(this.resources, R.drawable.column);
        this.obj_width = (float)this.columnImg.getWidth();
        this.obj_height = (float)this.columnImg.getHeight();
    }

    public void release() {
        if (!this.columnImg.isRecycled()) {
            this.columnImg.recycle();
        }

    }

    public Rect getObjRectTop() {
        this.rectTop.set((int)(this.obj_mid_x - this.obj_width / 2.0F), 0, (int)(this.obj_mid_x + this.obj_width / 2.0F), (int)(this.obj_mid_y - Config.COLUMN_Y_GAP / 2.0F));
        return this.rectTop;
    }

    public Rect getObjRectBottom() {
        this.rectBottom.set((int)(this.obj_mid_x - this.obj_width / 2.0F),
                (int)(this.obj_mid_y + Config.COLUMN_Y_GAP / 2.0F), (int)(this.obj_mid_x + this.obj_width / 2.0F), (int)Constants.SCREEN_HEIGHT);
        return this.rectBottom;
    }
}

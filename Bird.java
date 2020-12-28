package com.example.flybird;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bird extends GameObject {
    private Bitmap[] birdImgs;
    private Bitmap birdImg;
    private final double v0;
    private final double g;
    private final double t;
    private double speed;
    private double s;
    private float angle;
    private float groundHeight;
    private Rect obj_rect;

    public Bird(Resources resources, float groundHeight) {
        super(resources);
        this.v0 = Config.v0;
        this.g = Config.g;
        this.t = 0.6D;
        this.obj_rect = new Rect();
        this.groundHeight = groundHeight;
        this.initBitmap();
    }

    public void step() {
        double v1 = this.speed;
        double v = v1 - this.g * 0.6D;
        this.speed = v;
        this.s = v1 * 0.6D - 0.5D * this.g * 0.6D * 0.6D;
        this.obj_y -= (float)this.s;
        if (this.obj_y <= 0.0F) {
            this.obj_y = this.obj_height / 2.0F;
        }

        if (this.obj_y >= Constants.SCREEN_HEIGHT - this.groundHeight - this.obj_height) {
            this.obj_y = Constants.SCREEN_HEIGHT - this.groundHeight - this.obj_height;
        }

        if (this.speed >= 0.0D) {
            this.birdImg = this.birdImgs[this.currentFrame / 3];
            ++this.currentFrame;
            if (this.currentFrame == 9) {
                this.currentFrame = 0;
            }
        } else {
            this.birdImg = this.birdImgs[2];
        }

        this.angle = (float)(this.s * 4.0D);
        if (this.angle >= 30.0F) {
            this.angle = 30.0F;
        }

        if (this.angle <= -90.0F) {
            this.angle = -90.0F;
        }

        this.obj_mid_y = this.obj_y + this.obj_height / 2.0F;
        this.obj_rect.left = (int)(this.obj_x + (this.obj_width - this.obj_height) / 2.0F);
        this.obj_rect.top = (int)(this.obj_y + (this.obj_width - this.obj_height) / 2.0F);
        this.obj_rect.right = (int)((float)this.obj_rect.left + this.obj_height);
        this.obj_rect.bottom = (int)((float)this.obj_rect.top + this.obj_height - (this.obj_width - this.obj_height) / 2.0F);
    }

    public void flappy() {
        this.speed = this.v0;
    }

    public void drawSelf(Canvas canvas) {
        canvas.save();
        canvas.rotate(-this.angle, this.obj_mid_x, this.obj_mid_y);
        canvas.drawBitmap(this.birdImg, this.obj_x, this.obj_y, this.paint);
        canvas.restore();
    }

    public boolean pass(Column column) {
        return this.obj_mid_x <= column.getObjMidX() && column.getObjMidX() - this.obj_mid_x < 5.0F;
    }

    public boolean hitColumn(Column column) {
        return this.obj_rect.intersect(column.getObjRectTop()) || this.obj_rect.intersect(column.getObjRectBottom());
    }

    public boolean hitGround(Ground ground) {
        return this.obj_rect.bottom + 1 >= ground.getObjRect().top;
    }

    public void initBitmap() {
        this.birdImgs = new Bitmap[3];
        this.birdImgs[0] = BitmapFactory.decodeResource(this.resources, R.drawable.bird0);
        this.birdImgs[1] = BitmapFactory.decodeResource(this.resources, R.drawable.bird1);
        this.birdImgs[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird2);
        this.birdImg = this.birdImgs[0];
        this.obj_width = (float)this.birdImg.getWidth();
        this.obj_height = (float)this.birdImg.getHeight();
        this.obj_x = this.obj_width * 2.0F;
        this.obj_y = Constants.SCREEN_HEIGHT / 2.0F - this.obj_height / 2.0F;
        this.obj_mid_x = this.obj_x + this.obj_width / 2.0F;
        this.obj_mid_y = this.obj_y + this.obj_height / 2.0F;
    }

    public void release() {
        for(int i = 0; i < 3; ++i) {
            if (!this.birdImgs[i].isRecycled()) {
                this.birdImgs[i].recycle();
            }
        }

    }

    public Rect getObjRect() {
        return this.obj_rect;
    }
}

package com.example.flybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainView extends BaseView {
    private Ground ground;
    private Column column1;
    private Column column2;
    private Column column3;
    private Bird bird;
    private FileManager fileManager = new FileManager();
    private Bitmap bgImg;
    private Bitmap startImg;
    private Bitmap endImg;
    private Bitmap restartButtonImg;
    private Bitmap exitButtonImg;
    private Bitmap noticeImg;
    private Bitmap pauseButtonImg;
    private Bitmap bigNumbersImg;
    private Bitmap smallNumbersImg;
    private Bitmap medalImg;
    private float startImgX;
    private float startImgY;
    private float endImgX;
    private float endImgY;
    private float noticeImgX;
    private float noticeImgY;
    private float restartButtonImgX;
    private float restartButtonImgY;
    private float exitButtonImgX;
    private float exitButtonImgY;
    private float pauseButtonImgX;
    private float pauseButtonImgY;
    private float bigNumbersImgX;
    private float bigNumbersImgY;
    private float smallNumbersImgX;
    private float smallNumbersImgY;
    private float smallScoreX;
    private float smallScoreY;
    private float medalImgX;
    private float medalImgY;
    private boolean isStart = false;
    private boolean isHit = false;
    private boolean isOver = false;
    private boolean isPause = false;
    private boolean isWrite = false;
    private int score;
    private int bestScore;

    public MainView(Context context, SoundPlayer soundPlayer) {
        super(context, soundPlayer);
        if (this.fileManager.sdIsAvalible()) {
            this.fileManager.initFile();
            if (this.fileManager.fileReader().length() <= 0) {
                this.bestScore = 0;
            } else {
                this.bestScore = Integer.parseInt(this.fileManager.fileReader());
            }
        } else {
            Toast.makeText(this.mainActivity.getApplicationContext(), "SD卡不可用，将无法保存您的最高纪录", Toast.LENGTH_SHORT).show();
        }

        this.ground = new Ground(this.getResources());
        this.column1 = new Column(this.getResources(), Config.COLUMN_X_GAP * 2.0F, this.ground.getObjHeight());
        this.column2 = new Column(this.getResources(), Config.COLUMN_X_GAP + this.column1.getObjMidX(), this.ground.getObjHeight());
        this.column3 = new Column(this.getResources(), Config.COLUMN_X_GAP + this.column2.getObjMidX(), this.ground.getObjHeight());
        this.bird = new Bird(this.getResources(), this.ground.getObjHeight());
        this.thread = new Thread(this);
    }

    public void run() {
        while(this.threadFlag) {
            if (!this.isHit && !this.isOver) {
                this.ground.step();
            }

            if (this.isStart && !this.isHit && !this.isOver) {
                this.column1.step();
                this.column2.step();
                this.column3.step();
            }

            if (this.isStart) {
                this.bird.step();
            }

            this.drawSelf();
            if (this.isOver) {
                this.threadFlag = false;
            }

            if (this.isPause) {
                synchronized(this.thread) {
                    try {
                        this.thread.wait();
                    } catch (InterruptedException var11) {
                        var11.printStackTrace();
                    }
                }
            }

            try {
                Thread.sleep(16L);
            } catch (InterruptedException var10) {
                var10.printStackTrace();
            }
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var9) {
            var9.printStackTrace();
        }

        this.drawSelf();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

        this.drawNotice();
        if (this.fileManager.sdIsAvalible() && this.score > this.bestScore) {
            this.fileManager.fileWriter(String.valueOf(this.score));
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var7) {
            var7.printStackTrace();
        }

        for(int i = 0; i <= this.score; ++i) {
            this.drawResult(i);

            try {
                Thread.sleep(16L);
            } catch (InterruptedException var6) {
                var6.printStackTrace();
            }
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var5) {
            var5.printStackTrace();
        }

        synchronized(this.thread) {
            this.drawMedal();
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

        this.drawButton();
        this.isWrite = true;
    }

    public void drawSelf() {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.drawObject();
            if (!this.isHit) {
                if (this.bird.pass(this.column1) || this.bird.pass(this.column2) || this.bird.pass(this.column3)) {
                    this.soundPlayer.playSound(2, 0);
                    ++this.score;
                }

                if (this.bird.hitColumn(this.column1) || this.bird.hitColumn(this.column2) || this.bird.hitColumn(this.column3)) {
                    this.soundPlayer.playSound(3, 0);
                    this.paint.setAlpha(50);
                    this.paint.setColor(-1);
                    this.canvas.drawRect(0.0F, 0.0F, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, this.paint);
                    this.isHit = true;
                }
            }

            if (!this.isOver) {
                this.drawScore(this.bigNumbersImg, this.bigNumbersImgX, this.bigNumbersImgY, this.score);
            }

            if (this.isOver) {
                this.soundPlayer.playSound(5, 0);
                this.canvas.drawBitmap(this.endImg, this.endImgX, this.endImgY, this.paint);
            }

            if (!this.isOver && this.bird.hitGround(this.ground)) {
                this.soundPlayer.playSound(4, 0);
                this.isOver = true;
            }

            if (!this.isStart && !this.isStart) {
                this.canvas.drawBitmap(this.startImg, this.startImgX, this.startImgY, this.paint);
            }

            if (this.isStart && !this.isHit && !this.isOver) {
                if (!this.isPause) {
                    this.canvas.save();
                    this.canvas.clipRect(this.pauseButtonImgX, this.pauseButtonImgY, this.pauseButtonImgX + (float)this.pauseButtonImg.getWidth(),
                            this.pauseButtonImgY + (float)(this.pauseButtonImg.getHeight() / 2));
                    this.canvas.drawBitmap(this.pauseButtonImg, this.pauseButtonImgX, this.pauseButtonImgY, this.paint);
                    this.canvas.restore();
                } else {
                    this.canvas.save();
                    this.canvas.clipRect(this.pauseButtonImgX, this.pauseButtonImgY, this.pauseButtonImgX + (float)this.pauseButtonImg.getWidth(),
                            this.pauseButtonImgY + (float)(this.pauseButtonImg.getHeight() / 2));
                    this.canvas.drawBitmap(this.pauseButtonImg, this.pauseButtonImgX, this.pauseButtonImgY
                            - (float)(this.pauseButtonImg.getHeight() / 2), this.paint);
                    this.canvas.restore();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void drawNotice() {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.drawObject();
            this.soundPlayer.playSound(5, 0);
            this.canvas.drawBitmap(this.endImg, this.endImgX, this.endImgY, this.paint);
            this.canvas.drawBitmap(this.noticeImg, this.noticeImgX, this.noticeImgY, this.paint);
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void drawResult(int i) {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.drawObject();
            this.canvas.drawBitmap(this.endImg, this.endImgX, this.endImgY, this.paint);
            this.canvas.drawBitmap(this.noticeImg, this.noticeImgX, this.noticeImgY, this.paint);
            this.drawScore(this.smallNumbersImg, this.smallScoreX, this.smallScoreY, i);
        } catch (Exception var6) {
            var6.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void drawMedal() {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.drawObject();
            this.soundPlayer.playSound(5, 0);
            this.canvas.drawBitmap(this.endImg, this.endImgX, this.endImgY, this.paint);
            this.canvas.drawBitmap(this.noticeImg, this.noticeImgX, this.noticeImgY, this.paint);
            this.drawScore(this.smallNumbersImg, this.smallScoreX, this.smallScoreY, this.score);
            this.drawScore(this.smallNumbersImg, this.smallNumbersImgX, this.smallNumbersImgY, this.bestScore);
            this.drawMedalImg();
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void drawButton() {
        try {
            this.canvas = this.sfh.lockCanvas();
            this.drawObject();
            this.soundPlayer.playSound(5, 0);
            this.canvas.drawBitmap(this.endImg, this.endImgX, this.endImgY, this.paint);
            this.canvas.drawBitmap(this.noticeImg, this.noticeImgX, this.noticeImgY, this.paint);
            this.drawScore(this.smallNumbersImg, this.smallScoreX, this.smallScoreY, this.score);
            this.drawScore(this.smallNumbersImg, this.smallNumbersImgX, this.smallNumbersImgY, this.bestScore);
            this.drawMedalImg();
            this.canvas.drawBitmap(this.restartButtonImg, this.restartButtonImgX, this.restartButtonImgY, this.paint);
            this.canvas.drawBitmap(this.exitButtonImg, this.exitButtonImgX, this.exitButtonImgY, this.paint);
        } catch (Exception var5) {
            var5.printStackTrace();
        } finally {
            if (this.canvas != null) {
                this.sfh.unlockCanvasAndPost(this.canvas);
            }

        }

    }

    public void drawObject() {
        this.canvas.save();
        this.canvas.scale(this.scaleX, this.scaleY);
        this.canvas.drawBitmap(this.bgImg, 0.0F, 0.0F, this.paint);
        this.canvas.restore();
        this.column1.drawSelf(this.canvas);
        this.column2.drawSelf(this.canvas);
        this.column3.drawSelf(this.canvas);
        this.bird.drawSelf(this.canvas);
        this.ground.drawSelf(this.canvas);
    }

    public void drawMedalImg() {
        this.canvas.save();
        this.canvas.clipRect(this.medalImgX, this.medalImgY, this.medalImgX + (float)this.medalImg.getWidth(),
                this.medalImgY + (float)(this.medalImg.getHeight() / 2));
        if (this.score >= 60) {
            this.canvas.drawBitmap(this.medalImg, this.medalImgX, this.medalImgY - (float)(this.medalImg.getHeight() / 2), this.paint);
        } else {
            this.canvas.drawBitmap(this.medalImg, this.medalImgX, this.medalImgY, this.paint);
        }

        this.canvas.restore();
    }

    public void drawScore(Bitmap numbersImg, float x, float y, int num) {
        List<Integer> list = new ArrayList();
        int scoreCopy = num;

        int quotient;
        for(boolean var7 = false; (quotient = scoreCopy / 10) != 0; scoreCopy = quotient) {
            list.add(scoreCopy % 10);
        }

        list.add(scoreCopy % 10);
        float posY = y;
        int len = list.size();
        float oddNumW = (float)(numbersImg.getWidth() / 10);
        float oddNumH = (float)numbersImg.getHeight();
        float posX = x - (float)len * oddNumW / 2.0F;
        this.canvas.save();

        for(int i = len - 1; i >= 0; --i) {
            switch((Integer)list.get(i)) {
                case 0:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 0.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 1:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 1.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 2:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 2.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 3:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 3.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 4:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 4.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 5:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 5.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 6:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 6.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 7:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 7.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 8:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 8.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
                    break;
                case 9:
                    this.canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
                    this.canvas.drawBitmap(numbersImg, posX - 9.0F * oddNumW, posY, this.paint);
                    posX += oddNumW;
                    this.canvas.restore();
                    this.canvas.save();
            }
        }

        this.canvas.restore();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0 && event.getPointerCount() == 1) {
            float x = event.getX();
            float y = event.getY();
            if (this.isWrite) {
                if (x >= this.restartButtonImgX && x <= this.restartButtonImgX
                        + (float)this.restartButtonImg.getWidth() && y >= this.restartButtonImgY && y <= this.restartButtonImgY
                        + (float)this.restartButtonImg.getHeight()) {
                    this.mainActivity.getHandler().sendEmptyMessage(1);
                }

                if (x >= this.exitButtonImgX && x <= this.exitButtonImgX
                        + (float)this.exitButtonImg.getWidth() && y >= this.exitButtonImgY && y <= this.exitButtonImgY
                        + (float)this.exitButtonImg.getHeight()) {
                    this.mainActivity.getHandler().sendEmptyMessage(0);
                }
            }

            if (!this.isStart) {
                this.isStart = true;
            }

            if (!this.isHit && !this.isOver && (x <= this.pauseButtonImgX || x >= this.pauseButtonImgX
                    + (float)this.pauseButtonImg.getWidth() || y <= this.pauseButtonImgY || y >= this.pauseButtonImgY
                    + (float)(this.pauseButtonImg.getHeight() / 2))) {
                this.bird.flappy();
                this.soundPlayer.playSound(1, 0);
            }

            if (this.isStart && !this.isHit && !this.isOver && x >= this.pauseButtonImgX && x <= this.pauseButtonImgX
                    + (float)this.pauseButtonImg.getWidth() && y >= this.pauseButtonImgY && y <= this.pauseButtonImgY
                    + (float)(this.pauseButtonImg.getHeight() / 2)) {
                this.isPause = !this.isPause;
                if (!this.isPause) {
                    synchronized(this.thread) {
                        this.thread.notify();
                    }
                }
            }

            return true;
        } else {
            return false;
        }
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
        this.startImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.start);
        this.endImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.pausebutton);
        this.restartButtonImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.restartbutton);
        this.exitButtonImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.exitbutton);
        this.noticeImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.notice);
        this.pauseButtonImg = BitmapFactory.decodeResource(this.getResources(),  R.drawable.pausebutton);
        this.bigNumbersImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bignumbers);
        this.smallNumbersImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.smallnumbers);
        this.medalImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.medal);
        this.scaleX = Constants.SCREEN_WIDTH / (float)this.bgImg.getWidth();
        this.scaleY = Constants.SCREEN_HEIGHT / (float)this.bgImg.getHeight();
        this.startImgX = Constants.SCREEN_WIDTH / 2.0F - (float)(this.startImg.getWidth() / 2);
        this.startImgY = Constants.SCREEN_HEIGHT / 2.0F - (float)(this.startImg.getHeight() / 2);
        this.endImgX = Constants.SCREEN_WIDTH / 2.0F - (float)(this.endImg.getWidth() / 2);
        this.endImgY = Constants.SCREEN_HEIGHT / 2.0F - (float)(this.endImg.getHeight() * 3);
        this.noticeImgX = Constants.SCREEN_WIDTH / 2.0F - (float)(this.noticeImg.getWidth() / 2);
        this.noticeImgY = Constants.SCREEN_HEIGHT / 2.0F - (float)this.endImg.getHeight();
        this.restartButtonImgX = Constants.SCREEN_WIDTH / 2.0F - (float)(this.restartButtonImg.getWidth() * 5 / 4);
        this.restartButtonImgY = Constants.SCREEN_HEIGHT / 2.0F + (float)this.noticeImg.getHeight();
        this.exitButtonImgX = Constants.SCREEN_WIDTH / 2.0F + (float)(this.exitButtonImg.getWidth() / 4);
        this.exitButtonImgY = Constants.SCREEN_HEIGHT / 2.0F + (float)this.noticeImg.getHeight();
        this.pauseButtonImgX = 0.0F;
        this.pauseButtonImgY = Constants.SCREEN_HEIGHT - (float)(this.pauseButtonImg.getHeight() / 2);
        this.bigNumbersImgX = Constants.SCREEN_WIDTH / 2.0F;
        this.bigNumbersImgY = 10.0F;
        this.smallNumbersImgX = this.noticeImgX + (float)(this.noticeImg.getWidth() * 5 / 6);
        this.smallNumbersImgY = this.noticeImgY + (float)this.noticeImg.getHeight() - (float)(this.smallNumbersImg.getHeight() * 2);
        this.smallScoreX = this.smallNumbersImgX;
        this.smallScoreY = this.smallNumbersImgY - (float)(this.smallNumbersImg.getHeight() * 23 / 10);
        this.medalImgX = this.noticeImgX + (float)(this.noticeImg.getWidth() / 8);
        this.medalImgY = this.noticeImgY + (float)(this.noticeImg.getHeight() * 7 / 20);
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        super.surfaceDestroyed(arg0);
        this.release();
    }

    public void release() {
        if (!this.bgImg.isRecycled()) {
            this.bgImg.recycle();
        }

        if (!this.startImg.isRecycled()) {
            this.startImg.recycle();
        }

        if (!this.endImg.isRecycled()) {
            this.endImg.recycle();
        }

        if (!this.restartButtonImg.isRecycled()) {
            this.restartButtonImg.recycle();
        }

        if (!this.exitButtonImg.isRecycled()) {
            this.exitButtonImg.recycle();
        }

        if (!this.noticeImg.isRecycled()) {
            this.noticeImg.recycle();
        }

        if (!this.pauseButtonImg.isRecycled()) {
            this.pauseButtonImg.recycle();
        }

        if (!this.bigNumbersImg.isRecycled()) {
            this.bigNumbersImg.recycle();
        }

        if (!this.smallNumbersImg.isRecycled()) {
            this.smallNumbersImg.recycle();
        }

        if (!this.medalImg.isRecycled()) {
            this.medalImg.recycle();
        }

        this.ground.release();
        this.column1.release();
        this.column2.release();
        this.column3.release();
        this.bird.release();
    }
}

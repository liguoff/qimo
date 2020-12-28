package com.example.flybird;

public class Config {
    public static final int TO_MAIN_VIEW = 1;
    public static final int END_GAME = 0;
    public static final int LOADING_GAME_INTERVAL = 2000;
    public static final int SPEED;
    public static final float COLUMN_Y_GAP;
    public static final float COLUMN_X_GAP;
    public static final double v0;
    public static final double g;
    public static final double t = 0.6D;

    static {
        SPEED = (int)(Constants.SCREEN_WIDTH * 5.0F / 400.0F);
        COLUMN_Y_GAP = Constants.SCREEN_HEIGHT * 253.0F / 854.0F;
        COLUMN_X_GAP = Constants.SCREEN_WIDTH / 2.0F + 40.0F;
        v0 = (double)(Constants.SCREEN_HEIGHT * 23.0F / 854.0F);
        g = (double)(Constants.SCREEN_HEIGHT * 3.0F / 854.0F);
    }

    public Config() {
    }
}

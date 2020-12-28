package com.example.flybird;

import android.media.SoundPool;

import java.util.HashMap;


public class SoundPlayer {
    private SoundPool soundPool;
    private MainActivity mainActivity;
    private HashMap<Integer, Integer> map;

    public SoundPlayer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.map = new HashMap();
        this.soundPool = new SoundPool(8, 3, 0);
    }

    public void initSounds() {
        this.map.put(1, this.soundPool.load(this.mainActivity, R.raw.die, 1));
        this.map.put(2, this.soundPool.load(this.mainActivity, R.raw.flappy, 1));
        this.map.put(3, this.soundPool.load(this.mainActivity, R.raw.hit, 1));
        this.map.put(4, this.soundPool.load(this.mainActivity, R.raw.pass, 1));
        this.map.put(5, this.soundPool.load(this.mainActivity, R.raw.swooshing, 1));
    }

    public void playSound(int sound, int loop) {
        this.soundPool.play(sound, 1.0F, 1.0F, 1, loop, 1.0F);
    }
}

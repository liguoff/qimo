package com.example.flybird;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private LoadingView loadingView;
    private MainView mainView;
    private SoundPlayer soundPlayer;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MainActivity.this.toMainView();
            }

            if (msg.what == 0) {
                MainActivity.this.endGame();
            }

        }
    };

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        initPermission();
        this.getWindow().setFlags(1024, 1024);
        this.soundPlayer = new SoundPlayer(this);
        this.soundPlayer.initSounds();
        this.loadingView = new LoadingView(this, this.soundPlayer);
        this.setContentView(this.loadingView);
    }

    public void toMainView() {
        if (this.mainView == null) {
            this.mainView = new MainView(this, this.soundPlayer);
        } else {
            this.mainView = null;
            this.mainView = new MainView(this, this.soundPlayer);
        }

        this.setContentView(this.mainView);
        this.loadingView = null;
    }

    public void endGame() {
        if (this.mainView != null) {
            this.mainView.setThreadFlag(false);
        }

        if (this.loadingView != null) {
            this.loadingView.setThreadFlag(false);
        }

        this.finish();
    }

    public Handler getHandler() {
        return this.handler;
    }

    private void initPermission() {
        String permissions[] = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        ArrayList<String> toApplyList = new ArrayList<String>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }

        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

}

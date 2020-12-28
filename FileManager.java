package com.example.flybird;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileManager {
    private File sdDir;
    private File dirPath;
    private File file;

    public FileManager() {

    }

    public String fileReader() {
        String score = null;

        try {
            FileInputStream fin = new FileInputStream(this.file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            score = new  String(buffer);
            fin.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return "0";
    }

    public void fileWriter(String msg) {
//        Object var2 = null;
//
//        try {
//            FileOutputStream fout = new FileOutputStream(this.file);
//            byte[] buffer = msg.getBytes();
//            fout.write(buffer);
//            fout.close();
//        } catch (FileNotFoundException var5) {
//            var5.printStackTrace();
//        } catch (IOException var6) {
//            var6.printStackTrace();
//        }

    }

    public void initFile() {
        String dirpath = this.getSDPath() + File.separator + "FlyBird";
        this.dirPath = new File(dirpath);
        if (!this.dirPath.exists()) {
            try {
                boolean ok = this.dirPath.mkdir();Log.d(",", ok + "");
            } catch (Exception $e) {
                Log.d(",", $e.getMessage());
            }


        }

        String filePath = dirpath + File.separator + "score.txt";
        this.file = new File(filePath);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }

    public boolean sdIsAvalible() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public String getSDPath() {
        this.sdDir = Environment.getExternalStorageDirectory();
        return this.sdDir.toString();
    }
}

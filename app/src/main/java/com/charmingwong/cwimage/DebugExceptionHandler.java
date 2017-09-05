package com.charmingwong.cwimage;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by CharmingWong on 2017/5/17.
 */

public class DebugExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        e.printStackTrace();

        File file = new File(Environment.getExternalStorageDirectory(), "debug_info.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write((t.getName() + " : " + e.getMessage()).getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.exit(-1);
    }
}

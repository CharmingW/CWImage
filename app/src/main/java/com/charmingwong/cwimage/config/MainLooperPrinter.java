package com.charmingwong.cwimage.config;

import android.util.Log;
import android.util.Printer;

/**
 * Created by CharmingWong on 2017/7/10.
 */

public class MainLooperPrinter implements Printer {

    private static final String TAG = "MainLooperPrinter";
    private boolean mStartedPrinting;
    private long mStartTimeMillis;

    @Override
    public void println(String x) {
        if (!mStartedPrinting) {
            mStartTimeMillis = System.currentTimeMillis();
            mStartedPrinting = true;
        } else {
            final long endTime = System.currentTimeMillis();
            mStartedPrinting = false;
            if (isBlock(endTime)) {
                notifyBlockEvent();
            }
        }
    }

    private void notifyBlockEvent() {
        Log.i(TAG, "notifyBlockEvent: ");
    }

    private boolean isBlock(long endTime) {
        long blockThresholdMillis = 100;
        return endTime - mStartTimeMillis > blockThresholdMillis;
    }

}

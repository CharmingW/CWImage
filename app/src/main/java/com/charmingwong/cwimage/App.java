package com.charmingwong.cwimage;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import com.charmingwong.cwimage.config.MainLooperPrinter;
import com.charmingwong.cwimage.util.ApplicationUtils;
import com.github.moduth.blockcanary.BlockCanary;
import java.io.File;

/**
 * Created by CharmingWong on 2017/5/17.
 */

public class App extends Application {

    public static long start;

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static Handler getMainHandler() {
        return MAIN_HANDLER;
    }

    @Override
    public void onCreate() {
        start = System.currentTimeMillis();
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("theme", "0");
        setTheme(ApplicationUtils.getThemeId(Integer.parseInt(theme)));

//        Thread.setDefaultUncaughtExceptionHandler(new DebugExceptionHandler());

        File dir = new File(getExternalCacheDir(), "SoImage");
        if (!dir.exists()) {
            dir.mkdir();
        }

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        //ANR避免检测
        //StrictMode
        if (BuildConfig.DEBUG) {
            //线程策略，检测UI线程违规操作
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            //虚拟机策略，检测内存泄露
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        //BlockCanary
        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        Looper.getMainLooper().setMessageLogging(new MainLooperPrinter());
    }

}


package com.cookandroid.everytimecrawler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CrawlingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.e("com.cookandroid.everytimecrawler.CrawlingService","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.e("com.cookandroid.everytimecrawler.CrawlingService","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.util.Log.e("com.cookandroid.everytimecrawler.CrawlingService","onDestroy");
        super.onDestroy();
    }
}

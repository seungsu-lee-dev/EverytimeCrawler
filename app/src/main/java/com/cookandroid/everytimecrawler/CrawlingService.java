package com.cookandroid.everytimecrawler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

public class CrawlingService extends Service {
    ServiceControlEntity SC;
    ServiceControlDatabase sdb = ServiceControlDatabase.getInstance(this);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.e("CrawlingService","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.e("CrawlingService","onStartCommand");
        //String des = SC.getDes();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String d = sdb.ServiceControlDao().showDES();
                // OFF이면 스스로 서비스 종료(
                if(d == "OFF") {
                    stopSelf();
                }
            }
        }).start();



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.util.Log.e("CrawlingService","onDestroy");
        super.onDestroy();
    }
}

package com.cookandroid.everytimecrawler;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //여기에 딜레이 후 시작할 작업들을 입력
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean key = true;
                        while(key) {
                            String d = sdb.ServiceControlDao().showDes();
//                    Log.e("showDes",d);
                            // OFF이면 스스로 서비스 종료(
                            if (d.equals("OFF")) {
                                key = false;
                                stopSelf();
                            }
                        }
//                stopSelf();
                    }
                }).start();
            }
        }, 1000); // 3초 지연을 준 후 시작

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.util.Log.e("CrawlingService","onDestroy");
        super.onDestroy();
    }

//    private void loginData(String loginId, String loginPw, String cookie_key, String cookie_value, String userAgent) {
//        int temp_id = sdb.ServiceControlDao().showId();
//        System.out.println("temp_id는 " + temp_id);
//
//        String temp_title = sdb.ServiceControlDao().showTitle();
//        String temp_des = sdb.ServiceControlDao().showDes();
//
//        ServiceControlEntity SC = new ServiceControlEntity(temp_title, temp_des);
//
//        SC.setId(temp_id);
//        SC.setLoginId(loginId);
//        SC.setLoginPw(loginPw);
//        SC.setCookie_key(cookie_key);
//        SC.setCookie_value(cookie_value);
//        SC.setUserAgent(userAgent);
//
//        sdb.ServiceControlDao().update(SC);
//        System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + loginId + ", loginPw는 " + loginPw + ", cookie_key는 " + cookie_key + ", cookie_value는 " + cookie_value + ", userAgent는 " + userAgent);
//    }
}

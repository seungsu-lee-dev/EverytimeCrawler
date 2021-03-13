package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

public class loading extends MainActivity {
    private ImageView loadingImage;
    private Animation anim;
    ImageButton exitButton, preButton;
    boolean isPaused = false;

    ServiceControlDatabase sdb = ServiceControlDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        loadingImage = (ImageView) findViewById(R.id.loadingImage);
        exitButton = (ImageButton) findViewById(R.id.exitButton);
        preButton = (ImageButton) findViewById(R.id.preButton);

        initView();

        loadingImage.setOnClickListener(listener);
        preButton.setOnClickListener(listener);
        exitButton.setOnClickListener(listener);
    }

    private void initView(){
        anim = AnimationUtils.loadAnimation(this, R.anim.loding);
        loadingImage.setAnimation(anim);

        new Thread(new Runnable() {
            @Override
            public void run() {
                onoffData("ON");
            }
        }).start();
    }

    private void onoffData(String state) {
        int temp_id = sdb.ServiceControlDao().showId();
        System.out.println("temp_id는 " + temp_id);
        String c = "check";
        ServiceControlEntity SC = new ServiceControlEntity(c, state);
        SC.setId(temp_id);

        String temp_loginId = sdb.ServiceControlDao().showLoginId();
        System.out.println("temp_loginId는 " + temp_loginId);
        SC.setLoginId(temp_loginId);

        String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
        System.out.println("temp_loginPw는 " + temp_loginPw);
        SC.setLoginPw(temp_loginPw);

        String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
        System.out.println("temp_cookie_key는 " + temp_cookie_key);
        SC.setCookie_key(temp_cookie_key);

        String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
        System.out.println("temp_cookie_value는 " + temp_cookie_value);
        SC.setCookie_value(temp_cookie_value);

        String temp_userAgent = sdb.ServiceControlDao().showUserAgent();
        System.out.println("temp_userAgent는 " + temp_userAgent);
        SC.setUserAgent(temp_userAgent);

        sdb.ServiceControlDao().update(SC);
        System.out.println("c는 " + c + ", newc1은 " + state + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loadingImage:
                    if(!isPaused) {
                        loadingImage.clearAnimation();
                        // ON -> OFF

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                android.util.Log.i("run 완료", "Information message");
                                onoffData("OFF");
                            }
                        }).start();

//                        stopService(CrawlingService);
                    }
                    else {
                        loadingImage.startAnimation(anim);
                        // OFF -> ON

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                onoffData("ON");
                            }
                        }).start();
                    }
                    isPaused = !isPaused;
                    break;

                case R.id.preButton:
                    finish();
                    break;

                case R.id.exitButton:
                    Toast.makeText(loading.this, "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    break;
            }
        }
    };
}


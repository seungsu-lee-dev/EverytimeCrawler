package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

public class loading extends AppCompatActivity {
    private ImageView loadingImage;
    private Animation anim;
    ImageButton exitButton, preButton;
    boolean isPaused = false;
    Intent intent;
    Intent intent2;

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
        SC.setLoginId(temp_loginId);

        String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
        SC.setLoginPw(temp_loginPw);

        String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
        SC.setCookie_key(temp_cookie_key);

        String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
        SC.setCookie_value(temp_cookie_value);

        String temp_userAgent = sdb.ServiceControlDao().showUserAgent();
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
                        intent = new Intent(getApplicationContext(), CrawlingService.class);
                        startService(intent);
                    }
                    isPaused = !isPaused;
                    break;

                case R.id.preButton:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String d = sdb.ServiceControlDao().showDes();
                            Log.e("showDes", d);
//                             ON이면 loading 액티비티로 화면전환(
                            if (d.equals("ON")) {
                                intent2 = new Intent(getApplicationContext(), SubActivity.class);
                                startActivity(intent2);
                            }
                           finish();
                        }
                    }).start();

                    break;

                case R.id.exitButton:
                    Toast.makeText(loading.this, "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String d = sdb.ServiceControlDao().showDes();
                Log.e("showDes", d);
//                             ON이면 loading 액티비티로 화면전환(
                if (d.equals("ON")) {
                    intent2 = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(intent2);
                }
                finish();
            }
        }).start();
//        super.onBackPressed();
    }

}


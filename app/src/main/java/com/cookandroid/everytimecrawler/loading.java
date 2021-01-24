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

public class loading extends MainActivity {
    private ImageView loadingImage;
    private Animation anim;
    ImageButton exitButton, preButton;
    boolean isPaused = false;

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
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loadingImage:
                    if(!isPaused) {
                        loadingImage.clearAnimation();
                    }
                    else {
                        loadingImage.startAnimation(anim);
                    }
                    isPaused = !isPaused;
                    break;

                case R.id.preButton:
                    Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(intent);
                    break;

                case R.id.exitButton:
                    Toast.makeText(loading.this, "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    break;
            }
        }
    };
}


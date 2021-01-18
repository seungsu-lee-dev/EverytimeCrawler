package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class loading extends MainActivity {

    private ImageView imgAndroid;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initView();
    }
    private void initView(){
        imgAndroid = (ImageView) findViewById(R.id.imageView);
        anim = AnimationUtils.loadAnimation(this, R.anim.loding);
        imgAndroid.setAnimation(anim);
    }
    public void onClickImageButton2(View v){
        Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        startActivity(intent);
    }
}


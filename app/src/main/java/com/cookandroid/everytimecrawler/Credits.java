package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Credits extends AppCompatActivity {
    private TextView textView, textView2;
    private TextView title, soon, image_copyright;
    Intent intent;
    private Animation animation;

    LinearLayout page1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);


        animation =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);

        title = (TextView) findViewById(R.id.credit2);
        textView = (TextView) findViewById(R.id.credit);
        textView2 = (TextView) findViewById(R.id.credit3);
        soon = (TextView) findViewById(R.id.soon);
        page1 = findViewById(R.id.credit4);
        image_copyright = findViewById(R.id.image_copyright);

        title.setText("CREDITS \n");

        textView.setText("기획 및 제공\n\n" +
                "Project Manager\n\n"
                + "Full stack Developer\n\n"
                + "Front End developer\n\n"
                + "Designer\n\n"
                + "Thanks to contributor");

        textView2.setText("DOT\n\n" +
                " 이승수\n\n"
                + " 이승수, 이치형\n\n"
                + " 이동희\n\n"
                + " 이동희\n\n"
                + " 박경인");
        image_copyright.setText("\n\nrun, logout, trashcan Icons by Icons8.com");
        soon.setText("\n\nComing soon \n Mugunghwa Flowers Have Bloomed");

        page1.startAnimation(animation);
        setAnimation(animation);
    }

    //-------------------------< 자동으로 종료 >---------------------------
    public void setAnimation(Animation animation){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                intent = new Intent(Credits.this, MainActivity.class);
                //startActivity(intent);
                Credits.this.startActivity(intent);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

    }
}

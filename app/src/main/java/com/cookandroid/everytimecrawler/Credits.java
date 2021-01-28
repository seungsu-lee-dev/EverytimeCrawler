package com.cookandroid.everytimecrawler;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Credits extends AppCompatActivity {
    private TextView textView,textView2;
    private TextView title;

    private Animation animation, animation2;

    LinearLayout page1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);


        animation =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);
        animation2 =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits2);

        title = (TextView) findViewById(R.id.credit2);
        textView = (TextView) findViewById(R.id.credit);
        textView2 = (TextView) findViewById(R.id.credit3);

        page1 = findViewById(R.id.credit4);

        title.setText("CREDITS \n");

        textView.setText("기획 및 제공\n\n" +
                "Project Manager\n\n"
                +"Full stack Developer\n\n"
                +"Front End developer\n\n"
                +"Designer\n\n"
                +"Thanks to contributor");

        textView2.setText("DOT\n\n" +
                " 이승수\n\n"
                +" 이승수, 이치형\n\n"
                +" 이동희\n\n"
                +" 이동희\n\n"
                +" 박경인");

       /* title.startAnimation(animation2);
        textView.startAnimation(animation);
        textView2.startAnimation(animation);*/
        page1.startAnimation(animation);

    }

}

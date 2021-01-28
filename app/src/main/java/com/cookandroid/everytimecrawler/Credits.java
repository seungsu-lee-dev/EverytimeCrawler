package com.cookandroid.everytimecrawler;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Credits extends AppCompatActivity {
    private TextView textView;
    private TextView title;

    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        animation =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);


        title = (TextView) findViewById(R.id.credit2);
        textView = (TextView) findViewById(R.id.credit);


        title.setText("CREDITS \n" + "\n");

        textView.setText("기획 및 제공"+"                      DOT\n\n" +
                "project Manager"+"             이승수\n\n"
                +"Full stack Developer"+"      이승수,이치형\n\n"
                +"Front End developer"+"       이동희\n\n"
                +"Designer"+"                           이동희\n\n"
                +"Thanks to contributor"+"     박경인");

        title.startAnimation(animation);
        textView.startAnimation(animation);
    }

}

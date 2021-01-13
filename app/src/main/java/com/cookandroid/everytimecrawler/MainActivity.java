package com.cookandroid.everytimecrawler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    EditText login_id, login_password;
    String sId, sPw;
    Intent intent;
    ImageButton loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        login_id = (EditText) findViewById(R.id.login_id);
        login_password = (EditText) findViewById(R.id.login_password);

        intent = new Intent(this, CrawlingService.class);
        loginbutton = (ImageButton) findViewById(R.id.loginbutton);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(intent);
                android.util.Log.i("크롤링 인텐트로 넘어감", "startService()");
            }
        });
    }

//    public void loginbutton(View view) {
//        // 아이디, 비밀번호 받아온 값을 string으로
//        sId = login_id.getText().toString();
//        sPw = login_password.getText().toString();
//    }
}

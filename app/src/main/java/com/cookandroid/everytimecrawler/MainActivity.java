package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText login_id, login_password;
    String sId, sPw;
    Intent intent;
    ImageButton loginbutton;
    LoginThread thread;
    Button dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        login_id = (EditText) findViewById(R.id.login_id);
        login_password = (EditText) findViewById(R.id.login_password);

        loginbutton = (ImageButton) findViewById(R.id.loginbutton);
        dot = (Button) findViewById(R.id.btndot);
        dot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Credits.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);

                thread = new LoginThread();
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    android.util.Log.i("스레드 join 오류", "Information message");
                }
            }
        });
    }

    private class LoginThread extends Thread {
        public LoginThread() {
            //초기화 작업
            // 아이디, 비밀번호 받아온 값을 string으로
            sId = login_id.getText().toString();
            sPw = login_password.getText().toString();
        }

        public void run(){
            // 로그인 페이지 접속
            try {
                Connection.Response loginPageResponse = Jsoup.connect("https://everytime.kr/login")
                        .timeout(3000)
                        .header("Origin", "https://everytime.kr/")
                        .header("Referer", "https://everytime.kr")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
                        .method(Connection.Method.GET)
                        .execute();

                // 로그인 페이지에서 얻은 쿠키
                Map<String, String> loginTryCookie = loginPageResponse.cookies();

                // Window, Chrome의 User Agent.
                String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";

                // 전송할 폼 데이터
                Map<String, String> data = new HashMap<>();
                data.put("userid", sId);
                data.put("password", sPw);
                data.put("redirect", "/");

                // 로그인(POST)
                Connection.Response response = Jsoup.connect("https://everytime.kr/user/login")
                        .userAgent(userAgent)
                        .timeout(3000)
                        .header("Origin", "https://everytime.kr/")
                        .header("Referer", "https://everytime.kr")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
                        .cookies(loginTryCookie)
                        .data(data)
                        .method(Connection.Method.POST)
                        .execute();

                // 로그인 성공 후 얻은 쿠키.
                Map<String, String> loginCookie = response.cookies();

                Document doc = Jsoup.connect("https://everytime.kr/389368")
                        .userAgent(userAgent)
                        .timeout(3000000)
                        .cookies(loginCookie)
                        .get();

                String test_text = doc.text();
                System.out.println(test_text);

                if (test_text.contains("내 정보")) {
                    showToast("로그인되었습니다");
                }
                else {
                    showToast("로그인 정보가 올바르지 않습니다");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    final Handler mHandler = new Handler();
    void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

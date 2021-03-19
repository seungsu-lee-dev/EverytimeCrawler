package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText login_id, login_password;
    String sId, sPw;
    Intent intent;
    ImageButton loginbutton;
    LoginThread thread;
    AutoLoginThread auto_thread;
    Button dot;
    ServiceControlDatabase sdb;
    boolean auto_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sdb = ServiceControlDatabase.getInstance(MainActivity.this);
        auto_state = false;

        // assets/database/control_Database 접근이 가능한가?
        // ==> Room DB 사용하려면 스레드 사용해야 됨
        // 접근이 가능하다면 데이터를 넣거나 빼는게 가능한가?
        new Thread(new Runnable() {
            @Override
            public void run() {
                String c = "check";
                String c1 = "OFF";
                ServiceControlEntity SC = new ServiceControlEntity(c, c1);
                sdb = ServiceControlDatabase.getInstance(MainActivity.this);
                //sdb.ServiceControlDao().insert(SC);
                List<ServiceControlEntity> list = sdb.ServiceControlDao().getAll();
                Log.e("List", list.toString());

            }
        }).start();

//        auto_thread = new AutoLoginThread();
//        auto_thread.start();
//        try {
//            auto_thread.join();
//        } catch (InterruptedException e) {
////                    e.printStackTrace();
//            android.util.Log.i("스레드 join 오류", "Information message");
//        }
//
//        if(auto_state) {
//            auto_state = false;
//            intent = new Intent(getApplicationContext(), SubActivity.class);
//            startActivity(intent);
////            finish();
//        }

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

    private void loginData(String loginId, String loginPw, String cookie_key, String cookie_value, String userAgent) {
        int temp_id = sdb.ServiceControlDao().showId();
        System.out.println("temp_id는 " + temp_id);

        String temp_title = sdb.ServiceControlDao().showTitle();
        String temp_des = sdb.ServiceControlDao().showDes();

        ServiceControlEntity SC = new ServiceControlEntity(temp_title, temp_des);

        SC.setId(temp_id);
        SC.setLoginId(loginId);
        SC.setLoginPw(loginPw);
        SC.setCookie_key(cookie_key);
        SC.setCookie_value(cookie_value);
        SC.setUserAgent(userAgent);

        sdb.ServiceControlDao().update(SC);
        System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + loginId + ", loginPw는 " + loginPw + ", cookie_key는 " + cookie_key + ", cookie_value는 " + cookie_value + ", userAgent는 " + userAgent);
    }

    private boolean login(String loc_loginId, String loc_loginPw, String loc_userAgent) {
        try {
            // 전송할 폼 데이터
            Map<String, String> data = new HashMap<>();
            data.put("userid", loc_loginId);
            data.put("password", loc_loginPw);
            data.put("redirect", "/");

            // 로그인(POST)
            Connection.Response response = Jsoup.connect("https://everytime.kr/user/login")
                    .userAgent(loc_userAgent)
                    .timeout(3000)
                    .header("Origin", "https://everytime.kr/")
                    .header("Referer", "https://everytime.kr")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
                    .data(data)
                    .method(Connection.Method.POST)
                    .execute();

            // 로그인 성공 후 얻은 쿠키.
            Map<String, String> loginCookie = response.cookies();

            System.out.println(loginCookie);
            String cookie_key = "1", cookie_value = "1";

            for (Map.Entry<String, String> entry : loginCookie.entrySet()) {
                System.out.println("key는 " + entry.getKey() + ", value는 " + entry.getValue());
                cookie_key = entry.getKey();
                cookie_value = entry.getValue();
            }

            Document doc = Jsoup.connect("https://everytime.kr/389368")
                    .userAgent(loc_userAgent)
                    .timeout(3000000)
                    .cookies(loginCookie)
                    .get();

            String loc_test_text = doc.text();
            System.out.println(loc_test_text);

            if (loc_test_text.contains("내 정보")) {
                loginData(loc_loginId, loc_loginPw, cookie_key, cookie_value, loc_userAgent);
                return true;
            }
            else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private class AutoLoginThread extends Thread {
        public AutoLoginThread() {
            // 초기화 작업
        }
        public void run() {
            // 자동 로그인
                int temp_id = sdb.ServiceControlDao().showId();
                String temp_title = sdb.ServiceControlDao().showTitle();
                String temp_des = sdb.ServiceControlDao().showDes();
                String temp_loginId = sdb.ServiceControlDao().showLoginId();
                String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
                String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
                String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
                String temp_userAgent = sdb.ServiceControlDao().showUserAgent();

                System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent);

//                loginData(temp_loginId, temp_loginPw, temp_cookie_key, temp_cookie_value, temp_userAgent);

                boolean TF = login(temp_loginId, temp_loginPw, temp_userAgent);

                if (TF) {
                    showToast("자동로그인되었습니다");
                    auto_state = true;

//
//                    intent = new Intent(getApplicationContext(), SubActivity.class);
//                    startActivity(intent);
                }
                else {
                    showToast("로그인해주세요");
                }

        }
    }

    private class LoginThread extends Thread {
        public LoginThread() {
            // 초기화 작업
            // 아이디, 비밀번호 받아온 값을 string으로
            sId = login_id.getText().toString();
            sPw = login_password.getText().toString();
        }

        public void run(){
            // 로그인 페이지 접속
            // Window, Chrome의 User Agent.
                String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";

                boolean TF = login(sId, sPw, userAgent);

                if (TF) {
                    showToast("로그인되었습니다");
                }
                else {
                    showToast("로그인 정보가 올바르지 않습니다");
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

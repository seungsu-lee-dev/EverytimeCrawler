package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    EditText login_id, login_password;
    String sId, sPw;
    Intent intent;
    Intent intent2;
    ImageButton loginbutton;
    LoginThread thread;
    AutoLoginThread auto_thread;
    JumpLoadingThread jump_thread;
    Button dot;
    ServiceControlDatabase sdb;
    static boolean auto_state;
    static boolean loadingJump;
    static boolean login_state;
    static boolean testLogin_state;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sdb = ServiceControlDatabase.getInstance(MainActivity.this);
        auto_state = false;
        loadingJump = false;
        login_state = false;
        testLogin_state = false;

        // assets/database/control_Database 접근이 가능한가?
        // ==> Room DB 사용하려면 스레드 사용해야 됨
        // 접근이 가능하다면 데이터를 넣거나 빼는게 가능한가?
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String c = "check";
//                String c1 = "OFF";
//                ServiceControlEntity SC = new ServiceControlEntity(c, c1);
//                sdb = ServiceControlDatabase.getInstance(MainActivity.this);
//                //sdb.ServiceControlDao().insert(SC);
//                List<ServiceControlEntity> list = sdb.ServiceControlDao().getAll();
//                Log.e("List", list.toString());
//
//            }
//        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String d = sdb.ServiceControlDao().showDes();
//                Log.e("showDes", d);
//                // ON이면 loading 액티비티로 화면전환(
//                if (d.equals("ON")) {
//                    loadingJump = true;
//                    Log.e("loadingJump", String.valueOf(loadingJump));
//                    intent2 = new Intent(getApplicationContext(), loading.class);
//                    startActivity(intent2);
//                }
//            }
//        }).start();

//        if(loadingJump) {
//            loadingJump = false;
//            Log.e("loadingJump", String.valueOf(loadingJump));
//            intent2 = new Intent(getApplicationContext(), loading.class);
//            startActivity(intent2);
//        }

        login_id = (EditText) findViewById(R.id.login_id);
        login_password = (EditText) findViewById(R.id.login_password);

        loginbutton = (ImageButton) findViewById(R.id.loginbutton);
        dot = (Button) findViewById(R.id.btndot);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Credits.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                intent = new Intent(getApplicationContext(), SubActivity.class);
//                startActivity(intent);

                thread = new LoginThread();
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    android.util.Log.i("스레드 join 오류", "Information message");
                }
                if(testLogin_state||login_state) {
                    intent = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // 어플이 설치되어있지 않으면 마켓으로 이동
        if(!getPackageListCheck()) {
            try {
                // 플레이스토어가 없으면 웹페이지로 이동
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.everytime.v2")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.everytime.v2")));
            }
            Toast.makeText(MainActivity.this, "에브리타임 어플을 설치해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        jump_thread = new JumpLoadingThread();
        jump_thread.start();
        try {
            jump_thread.join();
            if(loadingJump) {
                Log.e("loadingJump", String.valueOf(loadingJump));
                intent2 = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent2);
                finish();
            }
            else {
                auto_thread = new AutoLoginThread();
                auto_thread.start();
                try {
                    auto_thread.join();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    android.util.Log.i("스레드 join 오류", "Information message");
                }
            }

            if (auto_state&&(!loadingJump)) {
                auto_state = false;
                intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
                finish();
            }

        } catch (InterruptedException e) {
//            e.printStackTrace();
            Log.i("점프 스레드 오류", "Information message");
        }
    }

    private boolean getPackageListCheck() {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        PackageInfo pi;

        try {
            pi = pkgMgr.getPackageInfo("com.everytime.v2", PackageManager.GET_ACTIVITIES);
            isExist = true;
        } catch(PackageManager.NameNotFoundException e) {
            isExist = false;
        }
        Log.d("isExist", Boolean.toString(isExist));
        return isExist;
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

        String temp_listnum = sdb.ServiceControlDao().showListnum();
        SC.setListnum(temp_listnum);

        sdb.ServiceControlDao().update(SC);
        System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + loginId + ", loginPw는 " + loginPw + ", cookie_key는 " + cookie_key + ", cookie_value는 " + cookie_value + ", userAgent는 " + userAgent + ", listnum은 " + temp_listnum);
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
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean testLogin(String loc_loginId, String loc_loginPw) {
        if((loc_loginId.equals("admin"))&&(loc_loginPw.equals("admin"))) {
            return true;
        }
        else {
            return false;
        }
    }

    private class JumpLoadingThread extends Thread {
        public JumpLoadingThread() {
            // 초기화 작업
        }
        public void run() {
            String d = sdb.ServiceControlDao().showDes();
            Log.e("showDes", d);
            // ON이면 loading 액티비티로 화면전환(
            if (d.equals("ON")) {
                loadingJump = true;
            }
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
            String temp_listnum = sdb.ServiceControlDao().showListnum();

            System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent + ", listnum은 " + temp_listnum);

//                loginData(temp_loginId, temp_loginPw, temp_cookie_key, temp_cookie_value, temp_userAgent, temp_listnum);

            boolean TF = login(temp_loginId, temp_loginPw, temp_userAgent);

            if (TF) {
                showToast("자동로그인되었습니다");
                auto_state = true;

//
//                    intent = new Intent(getApplicationContext(), SubActivity.class);
//                    startActivity(intent);
            } else {
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

        public void run() {
            // 로그인 페이지 접속
            // Window, Chrome의 User Agent.
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";

            boolean TF2 = testLogin(sId, sPw);

            if (TF2) {
                showToast("테스트 계정으로 접속합니다.");
                testLogin_state = true;
//                loginData("admin", "admin", "1", "1", "1");
            }
            else {
                boolean TF = login(sId, sPw, userAgent);

                if (TF) {
                    showToast("로그인되었습니다");
                    login_state = true;
                } else {
                    showToast("로그인 정보가 올바르지 않습니다");
                }
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

    @Override
    public void onBackPressed() {
        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish(); // 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("키워드 알람 어플 종료");
        alertDialog = alBuilder.create();
        alertDialog.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티를 종료하기 전
        if((alertDialog != null)&& (alertDialog.isShowing())) {
            alertDialog.dismiss();
        }
    }
}

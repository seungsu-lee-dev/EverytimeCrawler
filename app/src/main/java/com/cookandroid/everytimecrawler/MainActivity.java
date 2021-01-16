package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText login_id, login_password;
    String sId, sPw;
    Intent intent;
    ImageButton loginbutton;
    LoginThread thread;

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
                //startService(intent);
                //android.util.Log.i("크롤링 인텐트로 넘어감", "startService()");

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
//        private int threadNum = 0;
        public LoginThread() {
            //초기화 작업
//            this.threadNum = threadNum;
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
//                data.put("rememberLoginId", "1");
                data.put("redirect", "/");
//                data.put("ofp", ofp); // 로그인 페이지에서 얻은 토큰들
//                data.put("nfp", nfp);

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
                // 쿠키 중 TSESSION 이라는 값을 확인할 수 있다.
                Map<String, String> loginCookie = response.cookies();

                Document doc = Jsoup.connect("https://everytime.kr/389368")
                        .userAgent(userAgent)
                        .timeout(3000000)
                        .cookies(loginCookie)
                        .get();

                String test_text = doc.text();
                System.out.println(test_text);

//                Connection.Response initial = Jsoup.connect("https://everytime.kr/login")
//                        .method(Connection.Method.GET)
//                        .execute();
//
//                Map<String, String> data = new HashMap<>();
//                data.put("userid", sId);
//                data.put("password", sPw);
//                data.put("redirect", "/");
//
//                Connection.Response login = Jsoup.connect("https://everytime.kr/user/login")
//                        .cookies(initial.cookies())
//                        .data()
//                        .method(Connection.Method.POST)
//                        .timeout(5000)
//                        .execute();
//
//                Document doc = Jsoup.connect("https://everytime.kr/389368")
//                        .cookies(login.cookies())
//                        .timeout(3000000)
//                        .get();
//
//                String test_text = doc.text();
//                System.out.println(test_text);

            } catch (IOException e) {
                e.printStackTrace();
            }


//
//                // 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
//                Document loginPageDocument = null;
//                try {
//                    loginPageDocument = loginPageResponse.parse();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                String ofp = loginPageDocument.select("input.ofp").val();
//                String nfp = loginPageDocument.select("input.nfp").val();



//        // 티스토리 관리자 페이지
//        Document adminPageDocument = Jsoup.connect("http://partnerjun.tistory.com/admin")
//                .userAgent(userAgent)
//                .header("Referer", "http://www.tistory.com/")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .header("Accept-Encoding", "gzip, deflate, sdch")
//                .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
//                .cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
//                .get();
//
//        // select 내의 option 태그 요소들
//        Elements blogOptions = adminPageDocument.select("select.opt_blog > option");
//
//        // 블로그 이름과 url 얻어내기
//        for(Element option : blogOptions) {
//            String blogName = option.text();
//            String blogUrl = option.attr("abs:value");
//
//            System.out.println(blogName); // 간단한 블로그
//            System.out.println(blogUrl); // http://partnerjun.tistory.com/admin/center/
//                }


//            Document doc = null;
//            try {
//                doc = Jsoup.connect("https://everytime.kr/389368")
//                        .cookies(loginCookie)
//                        .timeout(3000000).get();
//            } catch (IOException e) {
////                e.printStackTrace();
//                android.util.Log.i("로그인 테스트용 장터게시판 크롤링 오류", "Information message");
//            }

//                Elements element = doc.select("a.article.h2.medium");

//                for (
//                        Element el : element.select("td.title.title-align")) {
//                    System.out.println(el.text());
//                }

//            String s = doc.select("a.article h2.medium").text();
//            System.out.println(s);
        }
    }
}

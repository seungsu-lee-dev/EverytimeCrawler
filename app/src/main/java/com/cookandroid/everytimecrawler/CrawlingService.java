package com.cookandroid.everytimecrawler;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CrawlingService extends Service {
    ServiceControlEntity SC;
    ServiceControlDatabase sdb = ServiceControlDatabase.getInstance(CrawlingService.this);
    Handler delayHandler = new Handler();
    CrawlingThread crawl_thread;
    static boolean isLive;
    static boolean isRun;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.e("CrawlingService", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.e("CrawlingService", "onStartCommand");
        //String des = SC.getDes();
        isLive = true;
        Timer timer = new Timer(isLive);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isRun = true;
                // 핸들러를 사용하여 딜레이 주기
                // 1초 후부터 DB의 des 필드 체크하여 OFF면 자동으로 서비스 종료
                delayHandler.post(new Runnable() {
                    public void run() {
                        //여기에 딜레이 후 시작할 작업들을 입력
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // isRun은 while문 무한루프 돌리기 위한 boolean 값
//                                isRun = true;
                                while (isRun) {
                                    String d = sdb.ServiceControlDao().showDes();
//                    Log.e("showDes",d);
                                    // OFF이면 스스로 서비스 종료(
                                    if (d.equals("OFF")) {
                                        isRun = false;
                                        stopSelf();
                                    }
                                }
//                stopSelf();
                            }
                        }).start();
                    }
                });
            }
        }, 1000, 1000); // 1초 지연을 준 후 1초마다 실행

        // Runnable 객체를 implent하는 방법(이것을 구현함으로써 한 번 실행될 객체를 정의가능)
        // 스레드를 만들고 그 안에 Runnable을 집어넣음
        // 스레드를 별도로 클래스로 만들었을 때와 차이가 없음
//        new Thread(new Runnable() {
//            boolean isRun = true;
//            @Override
//            public void run() {
//                // 30초마다 장터게시판 크롤링
//                while(isRun) {
//                    delayHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("크롤링 스레드 진입");
//                            int temp_id = sdb.ServiceControlDao().showId();
//                            String temp_title = sdb.ServiceControlDao().showTitle();
//                            String temp_des = sdb.ServiceControlDao().showDes();
//                            String temp_loginId = sdb.ServiceControlDao().showLoginId();
//                            String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
//                            String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
//                            String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
//                            String temp_userAgent = sdb.ServiceControlDao().showUserAgent();
//
//                            System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent);
//
//                            Map<String, String> temp_loginCookie = new HashMap<String, String>();
//                            temp_loginCookie.put(temp_cookie_key, temp_cookie_value);
//
//                            try {
//                                Document doc = Jsoup.connect("https://everytime.kr/389368")
//                                        .userAgent(temp_userAgent)
//                                        .timeout(3000000)
//                                        .cookies(temp_loginCookie)
//                                        .get();
//
//                                String loc_test_text = doc.text();
//                                System.out.println(loc_test_text);
//                                Elements timestamp = doc.select("a.article time");
//                                System.out.println(timestamp);
//
//
////                if (loc_test_text.contains("내 정보")) {
////
////                } else {
////
////                }
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    try {
//                        Thread.sleep(30000); // 30초마다 스레드를 재운다
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//        }).start();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isRun = true;
                delayHandler.post(new Runnable() {
                    public void run() {
                        //여기에 딜레이 후 시작할 작업들을 입력
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // isRun은 while문 무한루프 돌리기 위한 boolean 값
//                                isRun = true;
                                while (isRun) {
                                    crawl_thread = new CrawlingThread();
                                    crawl_thread.start();
                                    try {
                                        crawl_thread.join();
                                        isRun = false;
                                    } catch (InterruptedException e) {
                                        android.util.Log.i("크롤링 스레드 join 오류", "Information message");
                                    }
                                }
                            }
                        }).start();
                    }
                });
            }
        }, 2000, 30000); // 2초 지연을 준 후 30초마다 실행

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        android.util.Log.e("CrawlingService", "onDestroy");
        isLive = false;
        isRun = false;
        super.onDestroy();
    }

//    Handler delayHandler = new Handler() {
////    };

    private class CrawlingThread extends Thread {
        public CrawlingThread() {
            // 초기화 작업
        }

        public void run() {
            System.out.println("크롤링 스레드 진입");
            int temp_id = sdb.ServiceControlDao().showId();
            String temp_title = sdb.ServiceControlDao().showTitle();
            String temp_des = sdb.ServiceControlDao().showDes();
            String temp_loginId = sdb.ServiceControlDao().showLoginId();
            String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
            String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
            String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
            String temp_userAgent = sdb.ServiceControlDao().showUserAgent();

            System.out.println("title은 " + temp_title + ", des는 " + temp_des + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent);

            Map<String, String> temp_loginCookie = new HashMap<String, String>();
            temp_loginCookie.put(temp_cookie_key, temp_cookie_value);

            String strloginCookie = temp_cookie_key + "=" + temp_cookie_value;

            try {
//                Document doc = Jsoup.connect("https://everytime.kr/389368")
//                        .userAgent(temp_userAgent)
//                        .timeout(3000000)
//                        .cookies(temp_loginCookie)
//                        .get();
//
//                String loc_test_text = doc.text();
//                System.out.println(loc_test_text);
//                int t_len = loc_test_text.length();
//                System.out.println("문자열 길이 : " + t_len);
//                Element timestamp = doc.select("a.article").get(0);
//                // a.article time.small
//                System.out.println(timestamp.toString());

//                Document doc = Jsoup.connect("https://api.everytime.kr/find/board/article/list")
//                        .userAgent(temp_userAgent)
//                        .timeout(3000000)
//                        .cookies(temp_loginCookie)
//                        .post();
//
//                String loc_test_text = doc.text();
//                System.out.println(loc_test_text);

                Map<String, String> data = new HashMap<>();
                data.put("id", "389368");
                data.put("limit_num", "20");
                data.put("start_num", "0");
                data.put("moiminfo", "true");

//                Document doc = Jsoup.connect("https://api.everytime.kr/find/board/article/list")
//                        .userAgent(temp_userAgent)
//                        .timeout(3000)
//                        .header("Origin", "https://everytime.kr")
//                        .header("Referer", "https://everytime.kr/")
//                        .header("Accept", "*/*")
//                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .header("Accept-Encoding", "gzip, deflate, br")
//                        .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
//                        .data(data)
//                        .cookies(temp_loginCookie)
////                        .post()
//                        .parser(Parser.xmlParser()).post();

                Connection.Response response = Jsoup.connect("https://api.everytime.kr/find/board/article/list")
                        .userAgent(temp_userAgent)
                        .timeout(3000)
                        .header("Origin", "https://everytime.kr")
                        .header("Referer", "https://everytime.kr/")
                        .header("Accept", "*/*")
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
                        .header("Connection", "keep-alive")
                        .header("Content-Length", "48")
                        .header("Host", "api.everytime.kr")
                        .header("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"")
                        .header("sec-ch-ua-mobile", "?0")
                        .header("Sec-Fetch-Dest", "empty")
                        .header("Sec-Fetch-Mode", "cors")
                        .header("Sec-Fetch-Site", "same-site")
                        .data(data)
                        .cookies(temp_loginCookie)
//                        .post()
                        .method(Connection.Method.POST)
                        .execute();

                System.out.println("body출력");
                System.out.println(response.body());

                String crawl_text = response.body();
                String[] strArr = crawl_text.split(">");

                for(String s : strArr) {
                    System.out.println(s);
                }

//                String loc_test_text = doc.text();
//                System.out.println(loc_test_text);


//                Connection.Response response = Jsoup.connect("https://api.everytime.kr/find/board/article/list")
//                        .userAgent(temp_userAgent)
//                        .timeout(3000)
//                        .header("Origin", "https://everytime.kr")
//                        .header("Referer", "https://everytime.kr/")
//                        .header("Accept", "*/*")
//                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .header("Accept-Encoding", "gzip, deflate, br")
//                        .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
//                        .data(data)
//                        .cookies(temp_loginCookie)
//                        .method(Connection.Method.POST)
//                        .execute();





//                String source;
//                WebView webView = new WebView(CrawlingService.this);
//                //WebView 자바스크립트 활성화
//                webView.getSettings().setJavaScriptEnabled(true);
//                // 자바스크립트인터페이스 연결
//                // 이걸 통해 자바스크립트 내에서 자바함수에 접근할 수 있음.
//                webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
//                // 페이지가 모두 로드되었을 때, 작업 정의
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                        // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
//                        // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
//                        view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
//                    }
//                });
//
//                CookieManager cookieManager = CookieManager.getInstance();
//                cookieManager.setAcceptCookie(true);
//                List<Cookie> cookies = SessionControl.cookies;
//                cookieManager.removeAllCookie();
//                if (cookies != null) {
//                    for (Cookie cookie : cookies) {
//                        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Domain=" + cookie.getDomain();
//                        cookieManager.setCookie(cookie.getDomain(), cookieString);
//
//                    }
//                }
//
//                //지정한 URL을 웹 뷰로 접근하기
//                webView.loadUrl("https://everytime.kr/389368");


//                if (loc_test_text.contains("내 정보")) {
//
//                } else {
//
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

//    public class MyJavascriptInterface {
//        @JavascriptInterface
//        public void getHtml(String html) {
//            // 위 자바 스크립트가 호출되면 여기로 html이 반환됨
//            source = html;
//        }
//    }

}

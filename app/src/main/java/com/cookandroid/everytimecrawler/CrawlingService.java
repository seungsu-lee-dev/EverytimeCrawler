package com.cookandroid.everytimecrawler;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;
import com.cookandroid.everytimecrawler.Room.User;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    AppDatabase adb = AppDatabase.getInstance((CrawlingService.this));

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

        @RequiresApi(api = Build.VERSION_CODES.O)
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
//                System.out.println(response.body());

                String crawl_text = response.body();

                crawl_text = crawl_text.substring(crawl_text.indexOf("<article"));
                String[] strArr = crawl_text.split("\n");

                String temp_s = "";
                for(String s : strArr) {
//            	   strArr[0] += s;
                    temp_s += s;
                }

//               System.out.println(crawl_text);
//               System.out.println(temp_s);

                String[] strArr2 = temp_s.split("</article>|png\"/>");

//             String[] strArr3 = new String[20];
//             for(String s : strArr2) {
////          	   if()
////                 System.out.println(s);
//            	 int i=0;
//            	 strArr3[i] = s;
//            	 System.out.println(strArr3[i]);
//            	 i++;
//             }
//                for(int i=0;i<20;i++) {
//                    System.out.println("strArr2["+i+"] = " + strArr2[i]);
//                }

//             String format_time = format.format (cal.getTime());
//             System.out.println(format_time);
//             String now_time = format_time.substring(0, 13);
                LocalDateTime now_time = LocalDateTime.now();
                System.out.println(now_time);
//             String past_time =

                LocalDateTime past_time = now_time.minusHours(1);
                System.out.println(past_time);

//           Pattern p = Pattern.compile("(?<=created_at=\").*.(?=\")");
//           Matcher m = p.matcher(strArr2[0]);
//
//           boolean found = false;
//           while(m.find()) {
//        	   System.out.println(m.group().toString());
//        	   found = true;
//           }
//           if (!found) {
//        	    System.out.println("I didn't found the text");
//        	}

                // 게시글 번호
                int listnum = -1;

                String timestr = "";
                for(int i=0; i<20; i++) {
                    timestr = substringBetween(strArr2[i], "created_at=\"", "\"");
                    LocalDateTime time_value = LocalDateTime.parse(timestr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            	System.out.println(timestr);
//            	System.out.println(timestr.startsWith(now_time));

//                    Calendar calstr = CalendarFromString(timestr);
                    // 글 작성시간이 1시간 내면 true 리턴
//            	System.out.println(Boolean.toString(compareDate(past_time, now_time, timestr)));
                    boolean betweentime = compareDate(past_time, now_time, time_value);

                    // 1시간 내가 아니면 i를 저장하고 break;
                    if(!betweentime) {
                        listnum = i;
                        System.out.println("게시물 번호: "+listnum);
                        break;
                    }
                }

//                List userArr = adb.userDao().showDes();
//                List<User> users;
                String[] users;
                users = adb.userDao().showDes();
                System.out.println("키워드");
                System.out.println(users.length);
                int userlength = users.length;
//                System.out.println(users.get(0).getDes());
//                System.out.println(users[0]);
//                System.out.println(users[1]);
                for(int i=0; i<users.length; i++) {
                    users[i] = substringBetween(users[i],"[","]");
                    System.out.println(users[i]);
                }



                // text가 80자 미만이면 true
                boolean text_small;
                String textstr = "";
                String href = "";
                String titlestr = "";

                for(int i=0; i<(listnum+1); i++) {
                    titlestr = substringBetween(strArr2[i], "title=\"", "\"");
                    Log.d("제목", titlestr);

                    textstr = substringBetween(strArr2[i], "text=\"", "\"");

                    int textlength = textstr.length();
                    String textstr2 = "";
                    if (textlength == 80) {
                        text_small = false;
                        System.out.println(i + "번째 text가 80자 이상");
                        href = substringBetween(strArr2[i], "id=\"", "\"");
                        String texturl = "https://everytime.kr/389368/v/" + href;

                        Map<String, String> data3 = new HashMap<>();
                        data3.put("id", href);
                        data3.put("limit_number", "-1");
                        data3.put("moiminfo", "true");
                        Connection.Response response3 = Jsoup.connect("https://api.everytime.kr/find/board/comment/list")
                                .userAgent(temp_userAgent)
                                .timeout(3000)
                                .header("Origin", "https://everytime.kr")
                                .header("Referer", "https://everytime.kr/")
                                .header("Accept", "*/*")
                                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8")
                                .header("Connection", "keep-alive")
//                                .header("Content-Length", "39")
                                .header("Host", "api.everytime.kr")
                                .header("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"")
                                .header("sec-ch-ua-mobile", "?0")
                                .header("Sec-Fetch-Dest", "empty")
                                .header("Sec-Fetch-Mode", "cors")
                                .header("Sec-Fetch-Site", "same-site")
                                .data(data3)
                                .cookies(temp_loginCookie)
                                .method(Connection.Method.POST)
                                .execute();

                        System.out.println("");

                        textstr2 = response3.body();
//                        System.out.println(textstr2);

                        textstr2 = textstr2.substring(textstr2.indexOf("<article"));
                        String[] hrefArr = textstr2.split("\n");

                        String temptemp_s = "";
                        for(String s : hrefArr) {
                            temptemp_s += s;
                        }

                        String[] hrefArr2 = temptemp_s.split("</article>|png\"/>");
                        textstr2 = substringBetween(hrefArr2[0], "text=\"", "\"");
                        Log.d("내용", textstr2);



                        continue;
                    }
                    Log.d("내용", textstr);
                }


            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Boolean compareDate(LocalDateTime calStart, LocalDateTime calEnd, LocalDateTime calValue) throws ParseException {

        Boolean bValid = false;
        if (calStart.isBefore (calValue) && calEnd.isAfter(calValue)) {
            bValid = true;
        }

        return bValid;
    }

}

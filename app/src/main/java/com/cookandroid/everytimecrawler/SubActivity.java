package com.cookandroid.everytimecrawler;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlDatabase;
import com.cookandroid.everytimecrawler.Room.ServiceControlEntity;
import com.cookandroid.everytimecrawler.Room.User;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {
    //-------------------------< 전역 변수 >---------------------------------
    AppDatabase db;
    ServiceControlDatabase sdb;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    ImageButton btnAdd, btnDel, btnSave, btnSetting, btnLoad, btnRun, btnLogout;
    EditText editText;
    Intent intent;
    Intent intent1;
    Intent intent2;
    Intent intent3;
    Intent intent4;
    Intent intent5;
    Button btnImg;
    private String detail;
    private List<ServiceControlEntity> checks;
    ServiceControlEntity SC;
    LiveData<ServiceControlEntity> sdbTask;
    String packageName = "com.everytime.v2";
    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";
    AlertDialog alertDialog;

    //---------------------------------------------------------------------
    @Override // main같은 역할 / 클래스마다 다 필요 / 레이아수 생성,초기화 컴포넌트를 불러오는 역할
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //위에 상단바 안뜨게 하기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //sub main.xml 연결
        setContentView(R.layout.sub_main);

        ini();

        //불러오기
        if (detail != null) {
            detail = detail.replaceAll("\\[", "").replaceAll("\\]", "");
            String[] str = detail.split("\\s*,\\s*");
            for (int i = 0; i < str.length; i++) {
                Items.add(str[i]);
            }
        }

    }

    //------------------------< sub main과의 연결 >-------------------------
    private void ini() {
        db = AppDatabase.getInstance(this);
        sdb = ServiceControlDatabase.getInstance(this);
        Items = new ArrayList<String>();
        Adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item, Items);
        detail = getIntent().getStringExtra("data");

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(Adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnDel = (ImageButton) findViewById(R.id.btnDel);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnLoad = (ImageButton) findViewById(R.id.btnLoad);
        btnRun = (ImageButton) findViewById(R.id.btnRun);
        btnImg = (Button) findViewById(R.id.btnimg);
        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnLogout = (ImageButton) findViewById(R.id.btnlogout);

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnLoad.setOnClickListener(listener);
        btnRun.setOnClickListener(listener);
        btnImg.setOnClickListener(listener);
        btnSetting.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnLogout.setOnClickListener(listener);

    }

    //-------------------------< copyright 부분 >-----------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //데이터 받기
            if (requestCode == 4) {
                com.lakue.lakuepopupactivity.PopupResult result = (com.lakue.lakuepopupactivity.PopupResult) data.getSerializableExtra("result");
                if (result == com.lakue.lakuepopupactivity.PopupResult.LEFT) {
                    // 작성 코드
                    //Toast.makeText(this, "LEFT", Toast.LENGTH_SHORT).show();

                } else if (result == com.lakue.lakuepopupactivity.PopupResult.RIGHT) {
                    // 작성 코드
                    String url = "https://github.com/seungsu-lee-dev/EverytimeCrawler";
                    intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent5);
                    //Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();

                } else if (result == com.lakue.lakuepopupactivity.PopupResult.IMAGE) {
                    // 작성 코드
                    //Toast.makeText(this, "IMAGE", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    //-------------------------< savelist 부분 >-----------------------------------
    private void make_title() {
        EditText et = new EditText(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
        builder.setTitle("제목을 입력하세요");
        builder.setView(et);

        builder.setPositiveButton("저장", (dialog, which) -> {
            String s = et.getText().toString();
            // db에 저장하기
            ArrayList<String> itemList = new ArrayList<>();
            for (int i = 0; i < listView.getCount(); i++) {
                String s1 = (String) listView.getItemAtPosition(i);
                itemList.add(s1);
            }
            String s1 = String.valueOf(itemList);
            User memo = new User(s,s1);
            db.userDao().insert(memo);
            Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        });

        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }

//    private void createData() {
//        String c = "check";
//        String c1 = "ON";
//        ServiceControlEntity SC = new ServiceControlEntity(c, c1);
//        sdb.ServiceControlDao().insert(SC);
//    }
//
//    private void offData() {
//        String c = "check";
//        String newc1 = "OFF";
//        ServiceControlEntity SC = new ServiceControlEntity(c, newc1);
//        sdb.ServiceControlDao().update(SC);
//    }
//
//    private void onData() {
//        String c = "check";
//        String newc2 = "ON";
//        ServiceControlEntity SC = new ServiceControlEntity(c, newc2);
//        sdb.ServiceControlDao().update(SC);
//    }

//    //boolean
//    private void returnTrueFalse() {
//        checks = ServiceControlDatabase.getInstance(this).ServiceControlDao().getAll();
//        System.out.println(checks);
////        if() {
////            return true;
////        }
////        else {
////            return false;
////        }
//    }

    private void logout() {
        int temp_id = sdb.ServiceControlDao().showId();
        System.out.println("temp_id는 " + temp_id);

        String temp_title = sdb.ServiceControlDao().showTitle();
        String temp_des = sdb.ServiceControlDao().showDes();

        ServiceControlEntity SC = new ServiceControlEntity(temp_title, temp_des);

        SC.setId(temp_id);
        SC.setLoginId("1");
        SC.setLoginPw("1");
        SC.setCookie_key("1");
        SC.setCookie_value("1");
        SC.setUserAgent("1");

//        String temp_listnum = sdb.ServiceControlDao().showListnum();
        SC.setListnum("1");

        sdb.ServiceControlDao().update(SC);
    }

    private void onoffData(String state) {
        int temp_id = sdb.ServiceControlDao().showId();
        System.out.println("temp_id는 " + temp_id);
        String c = "check";
        ServiceControlEntity SC = new ServiceControlEntity(c, state);
        SC.setId(temp_id);

        String temp_loginId = sdb.ServiceControlDao().showLoginId();
        SC.setLoginId(temp_loginId);

        String temp_loginPw = sdb.ServiceControlDao().showLoginPw();
        SC.setLoginPw(temp_loginPw);

        String temp_cookie_key = sdb.ServiceControlDao().showCookie_key();
        SC.setCookie_key(temp_cookie_key);

        String temp_cookie_value = sdb.ServiceControlDao().showCookie_value();
        SC.setCookie_value(temp_cookie_value);

        String temp_userAgent = sdb.ServiceControlDao().showUserAgent();
        SC.setUserAgent(temp_userAgent);

        String temp_listnum = sdb.ServiceControlDao().showListnum();
        SC.setListnum(temp_listnum);

        sdb.ServiceControlDao().update(SC);
        System.out.println("c는 " + c + ", newc1은 " + state + ", loginId는 " + temp_loginId + ", loginPw는 " + temp_loginPw + ", cookie_key는 " + temp_cookie_key + ", cookie_value는 " + temp_cookie_value + ", userAgent는 " + temp_userAgent + ", listnum은 " + temp_listnum);
    }

    private void deleteData() {
        sdb.ServiceControlDao().deleteAll();
    }

    //------------------------< sub main 버튼 부분 >----------------------------------
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // 추가 , 삭제 , 리스트 저장 , 리스트불러오기 , 세팅 , 런
            switch (v.getId()) {
                case R.id.btnSave:
                    make_title();
                    break;

                case R.id.btnSetting:
////                    createData();
////                    returnTrueFalse();
////                    modifyData();
////                    returnTrueFalse();
////                    String t = SC.getTitle();
////                    System.out.println(t);
////                    deleteData();
////                    sdbTask = sdb.ServiceControlDao().loadlastTask();
////                    List<ServiceControlEntity> list = sdb.ServiceControlDao().getAll();
////                    if(list == null) {
////                    if(sdbTask.getValue() == null) {
//////                    if(sdb == null) {
////                        // table is empty
////                        android.util.Log.i("테이블이 비어있음", "Information message");
////                        createData();
////                    } else {
////                        // table is not empty
////                        String des = SC.getDes();
////                        // OFF이면 ON으로 변경
////                        if(des == "OFF") {
////                            SC.setDes("ON");
//////                            onData();
////                            break;
////                        }
////                    }
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //checks = sdb.ServiceControlDao().getAll();
//                            //int size = checks.size();
//                            //for( int i = 0; i < size; i++) {
//                            //    checks.get(i);
//                            //    System.out.println(checks);
//                            //}
//
//                            //String t = SC.getTitle();
//                            String t = sdb.ServiceControlDao().showTitle();
//                            if(t.equals("check")) {
//                                // table title이 check이면
//                                android.util.Log.i("title은 check", "Information message");
//                                //String des = SC.getDes();
//                                String d = sdb.ServiceControlDao().showDes();
//                                if(d.equals("OFF")) {
//                                    android.util.Log.i("des는 OFF", "Information message");
//                                    //SC.setDes("ON");
//                                    onData();
//                                }
//                                android.util.Log.i("des는 ON", "Information message");
//                            } else {
//                                System.out.println(t);
//                                android.util.Log.i("controlTable 가져오기 오류", "Information message");
//                            }
//                        }
//                    }).start();
                    Items.clear();
                    Adapter.notifyDataSetChanged();
                    break;

                case R.id.btnimg:
                    intent = new Intent(getBaseContext(), PopupActivity.class);
                    intent.putExtra("type", PopupType.IMAGE);
                    intent.putExtra("title", "https://blogfiles.pstatic.net/MjAyMTAxMjZfMjM4/MDAxNjExNjYwNDU1MTUz.Z6IkQhuBa-O6BmiBfnlWybZR8iBQ0CSwN6RlIFqsYagg.Gqlc7lJGfYCHJjShjm_wO5FsH0PShs5ZNsrQjNqGoukg.PNG.hhhh7611/abc.png");
                    intent.putExtra("buttonLeft", "종료");
                    intent.putExtra("buttonRight", "바로가기");
                    startActivityForResult(intent, 4);
                    break;

                case R.id.btnAdd:
                    String text = editText.getText().toString();
                    if (text.length() != 0) {
                        Items.add(text);
                        editText.setText("");
                        Adapter.notifyDataSetChanged();
                    }
                    break;

                case R.id.btnDel:
                    int pos;
                    pos = listView.getCheckedItemPosition();
                    if (pos != ListView.INVALID_POSITION) {
                        Items.remove(pos);
                        listView.clearChoices();
                        Adapter.notifyDataSetChanged();
                    }
                    break;

                case R.id.btnLoad:
                    intent = new Intent(getApplicationContext(),LoadList.class);
                    startActivity(intent);
//                    finish();
                    break;

                case R.id.btnRun:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onoffData("ON");
                        }
                    }).start();

                    intent1 = new Intent(getApplicationContext(), CrawlingService.class);
                    startService(intent1);
//                    android.util.Log.i("크롤링 인텐트로 넘어감", "startService()");
                    android.util.Log.i("크롤링 인텐트로 넘어감", "Information message");
                    intent2 = new Intent(getApplicationContext(), loading.class);
                    startActivity(intent2);
//                    push();
                    Toast.makeText(SubActivity.this, "키워드 알람 기능이 시작됩니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case R.id.btnlogout:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            onoffData("OFF");
                            logout();
                        }
                    }).start();

                    intent4 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent4);
                    finish();
                    break;
            }
        }
    };

    //---------------------------------------- 상단바 알림 -------------------------------------------------------
    private void push(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel( new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }
        else{
            builder = new NotificationCompat.Builder(this);
        }

        intent3 = this.getPackageManager().getLaunchIntentForPackage(packageName);
        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent3,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("알림");
        builder.setContentText("알림 메시지");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1,notification);
    }

    //------------------------< enum 부분 >-----------------------------------------
    enum PopupGravity {
        CENTER,RIGHT,LEFT
    }
    enum PopupResult{
        LEFT,CENTER,RIGHT,IMAGE
    }
    enum Type{
        NORMAL, SELECT, ERROR, IMAGE
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
                Toast.makeText(SubActivity.this, "어플을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish(); // 현재 액티비티를 종료한다. (SubActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
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

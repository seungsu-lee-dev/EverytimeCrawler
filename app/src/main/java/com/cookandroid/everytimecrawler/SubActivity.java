package com.cookandroid.everytimecrawler;


import android.content.Intent;
import android.os.Bundle;
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

import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.User;
import com.lakue.lakuepopupactivity.PopupActivity;
import com.lakue.lakuepopupactivity.PopupResult;
import com.lakue.lakuepopupactivity.PopupType;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    //-------------------------< 전역 변수 >---------------------------------
    AppDatabase db;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    ImageButton btnAdd, btnDel, btnSave, btnSetting, btnLoad, btnRun;
    EditText editText;
    Intent intent;
    Intent intent1;
    Intent intent2;
    Button btnImg;
    private String detail;

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

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnLoad.setOnClickListener(listener);
        btnRun.setOnClickListener(listener);
        btnImg.setOnClickListener(listener);
        btnSetting.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);

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
                    Toast.makeText(this, "LEFT", Toast.LENGTH_SHORT).show();

                } else if (result == com.lakue.lakuepopupactivity.PopupResult.RIGHT) {
                    // 작성 코드
                    Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();

                } else if (result == com.lakue.lakuepopupactivity.PopupResult.IMAGE) {
                    // 작성 코드
                    Toast.makeText(this, "IMAGE", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }//

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

    //------------------------< sub main 버튼 부분 >----------------------------------
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) { // 추가 , 삭제 , 리스트 저장 , 리스트불러오기 , 세팅 , 런
            switch (v.getId()) {
                case R.id.btnSave:
                    make_title();
                    break;
                case R.id.btnSetting:
                    break;

                case R.id.btnimg:
                    Intent intent = new Intent(getBaseContext(), PopupActivity.class);
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
                    intent1 = new Intent(getApplicationContext(), CrawlingService.class);
                    startService(intent1);
//                    android.util.Log.i("크롤링 인텐트로 넘어감", "startService()");
                    android.util.Log.i("크롤링 인텐트로 넘어감", "Information message");
                    intent2 = new Intent(getApplicationContext(), loading.class);
                    startActivity(intent2);
                    break;
            }
        }
    };

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
}
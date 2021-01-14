package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    ImageButton setting11;

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);

        items = new ArrayList<String>();
        items.add("Sunday");
        items.add("Monday");
        items.add("Tuesday");
        items.add("Wednesday");
        items.add("Thursday");
        items.add("Friday");
        items.add("Saturday");

        adapter = new ArrayAdapter<String>(SubActivity.this,
                android.R.layout.simple_list_item_single_choice, items);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    setting11 = (ImageButton) findViewById(R.id.setting11);
    setting11.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    });

// add,delete 버튼 클릭시 실행되는 메소드 -----------------------------------------------
        public void btnDelete(View v) {
            EditText ed = (EditText) findViewById(R.id.newitem);
            switch (v.getId()) {
                case R.id.btnAdd:                                 // ADD 버튼 클릭시
                    String text = ed.getText().toString();        // EditText에 입력된 문자열값을 얻기
                    if (!text.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
                        items.add(text);                          // items 리스트에 입력된 문자열 추가
                        ed.setText("");                           // EditText 입력란 초기화
                        adapter.notifyDataSetChanged();           // 리스트 목록 갱신
                    }
                    break;
                case R.id.btnDelete:                             // DELETE 버튼 클릭시
                    int pos = listView.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
                    if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
                        items.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
                        listView.clearChoices();                 // 선택 해제
                        adapter.notifyDataSetChanged();
                        // 어답터와 연결된 원본데이터의 값이 변경된을 알려 리스트뷰 목록 갱신
                    }
                    break;
            }
        }

 //각 목록 버튼 클릭 시 실행되는 메소드 --------------------------------------------
        public void getList(View v) {
            switch(v.getId()) {
                case R.id.btnWeekList: // 요일목록
                    // 기존 items 리스트의 데이터를 초기화하고 새로 추가
                    items.clear();
                    items.add("Sunday");
                    items.add("Monday");
                    items.add("Tuesday");
                    adapter.notifyDataSetChanged(); // 갱신

                    break;
                case R.id.btnBookList: // 도서목록
                    // 기존 items 리스트의 데이터를 초기화하고 새로 추가
                    items.clear();
                    items.add("자바의 정석");
                    items.add("토비의 스프링");
                    items.add("안드로이드 정석");
                    adapter.notifyDataSetChanged(); //갱신
                    break;
            }
        }
        }





}
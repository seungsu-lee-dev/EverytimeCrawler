package com.cookandroid.everytimecrawler;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.User;
import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {
    AppDatabase db;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    ImageButton btnAdd, btnDel, btnSave, btnSetting, btnLoad, btnRun;
    EditText editText;
    Intent intent;
    Intent intent1;
    Intent intent2;
    private String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sub_main);

        db = AppDatabase.getInstance(this);
        Items = new ArrayList<String>();
        Adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item, Items);
        detail = getIntent().getStringExtra("data");

        ini();

        //불러오기
        if(detail != null ) {
            detail = detail.replaceAll("\\[","").replaceAll("\\]","");
            String [] str = detail.split("\\s*,\\s*");
            for(int i = 0; i < str.length; i++) {
                Items.add(str[i]);
            }
        }

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_title();
            }
        });
    }

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

    private void ini() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(Adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnDel = (ImageButton) findViewById(R.id.btnDel);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnLoad = (ImageButton) findViewById(R.id.btnLoad);
        btnRun = (ImageButton) findViewById(R.id.btnRun);

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnLoad.setOnClickListener(listener);
        btnRun.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
                    finish();
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
}
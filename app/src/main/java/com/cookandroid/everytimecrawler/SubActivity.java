package com.cookandroid.everytimecrawler;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.User;
import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {

    AppDatabase db;
    ImageButton setting11;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    ImageButton btnAdd, btnDel, btnSave, btnLoad;
    EditText editText;
    Intent intent;
    String kiword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);

        intent = getIntent();
        kiword = intent.getStringExtra("data");

        db = AppDatabase.getInstance(this);
        Items = new ArrayList<String>();
        Adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item, Items);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(Adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnDel = (ImageButton) findViewById(R.id.btnDel);
        btnSave = (ImageButton) findViewById(R.id.savelist11);
        btnLoad = (ImageButton) findViewById(R.id.loadlist1);

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);
        btnLoad.setOnClickListener(listener);


        setting11 = (ImageButton) findViewById(R.id.setting11);

       /* setting11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> gs = new ArrayList<>();
                String gs2 = getIntent().getStringExtra("data");
                String x = (String) gs2;
                gs.add(x);
                for(int i = 0; i < gs.size(); i++) {
                    Items.add(gs.get(i));
                }

            }
        });*/


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
            String s1 = (String) itemList.toString();
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

                case R.id.loadlist1:
                    Intent intent = new Intent(getApplicationContext(),LoadList.class);
                    startActivity(intent);
                    finish();
            }

        }



    };
}







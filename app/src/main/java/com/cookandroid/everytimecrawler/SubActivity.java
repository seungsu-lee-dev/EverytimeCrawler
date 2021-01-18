package com.cookandroid.everytimecrawler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {

//--------------------------< 객체 선언 > --------------------------
    ImageButton setting11;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    ImageButton btnAdd, btnDel;
    EditText editText;
    ImageButton save1,load;
//-------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main); // sub_main의 레이아웃과 연결

        Items = new ArrayList<String>();

        // findViewById : 해당 아이디로부터 대응되는 뷰객체를 찾는 메서드

        //  listview 객체랑 activity_main의 R.id.listView랑 연결
        listView = (ListView) findViewById(R.id.listView);

        // 어텝터 생성
        Adapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item, Items);

        // 어뎁터 설정 / 위에 adater설정한거를 listview에 넣는다
        listView.setAdapter(Adapter);


        // 리스트뷰에 choicemode 속성을 설정해야 선택기능 사용가능 / 여기말고 main.xml에서도 설정가능
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        editText = (EditText) findViewById(R.id.editText);

        load = (ImageButton) findViewById(R.id.loadlist1);
//-----------------------< add del >------------------------------------
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnDel = (ImageButton) findViewById(R.id.btnDel);

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);

//-------------------------- setting 버튼 부분 -----------------------------------

        setting11 = (ImageButton) findViewById(R.id.setting11);

        setting11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent11 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent11);
            }
        });

        save1 = (ImageButton) findViewById(R.id.savelist11);



    }


//-----------------------------------------------------------------------------------
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.savelist11:
//                startActivity(new Intent(this, Dialog.class));
//                break;
 //       }
    //}
//---------------------------<  add , del 버튼 >-------------------------------------
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAdd:
                    String text = editText.getText().toString();
                    if (text.length() != 0) {
                        Items.add(text);
                        editText.setText("");
                        Adapter.notifyDataSetChanged(); // listview 갱신할때 사용하는 메소드
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
            }

        }
    };

//-------------------------------- 팝업창 ------------------------------
    public void OnClickHandler(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("").setMessage("목록이 추가되었습니다");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}







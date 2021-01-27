package com.cookandroid.everytimecrawler;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.everytimecrawler.Recycle.RecyclerAdapter;
import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoadList extends AppCompatActivity {
    private FloatingActionButton add;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);

        initialized();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(view -> {
            move();
        });
    }

    private void initialized() {
        add = findViewById(R.id.addMemo);

        recyclerView = findViewById(R.id.mainRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerAdapter();

//        int size = AppDatabase.getInstance(this).userDao().getAll().size();
        users = AppDatabase.getInstance(this).userDao().getAll();
        int size = users.size();
        for(int i = 0; i < size; i++) {
            //adapter.addItems(AppDatabase.getInstance(this).userDao().getAll().get(i));
            adapter.addItems(users.get(i));
        }
    }

    public void move() {
//        Intent intent = new Intent(getApplicationContext(),SubActivity.class);
//        startActivity(intent);
        finish();
    }
}

package com.cookandroid.everytimecrawler.Recycle;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cookandroid.everytimecrawler.R;
import com.cookandroid.everytimecrawler.Room.AppDatabase;
import com.cookandroid.everytimecrawler.Room.User;
import com.cookandroid.everytimecrawler.SubActivity;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<User> userData = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memorecycler_itemview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(userData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public void addItems(User user) {
        userData.add(user);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView key;
        private TextView title;
        private TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            key = itemView.findViewById(R.id.key);
            title = itemView.findViewById(R.id.TITLE);
            description = itemView.findViewById(R.id.textview3);
        }

        public void onBind(User user, int position) {
            String s = "" + (position+1);
            key.setText(s);
            title.setText(user.getTitle());
            description.setText(user.getDes());

            itemView.setOnLongClickListener(view -> {
                userData.remove(user);
                AppDatabase.getInstance(itemView.getContext()).userDao().delete(user);
                notifyDataSetChanged();
                return false;
            });
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), SubActivity.class);
                intent.putExtra("data", user.getDes());
                itemView.getContext().startActivity(intent);

            });
        }
    }
}
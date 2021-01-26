package com.cookandroid.everytimecrawler.Room;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "kTable")
public class User implements Parcelable {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  String title;
    private  String des;

    public User(String title, String des) {
        this.title = title;
        this.des = des;
    }

    protected User(Parcel in) {
        id = in.readInt();
        title = in.readString();
        des = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDes() {return des;}

    public void setDes(String des) {this.des = des;}

    public String getTitle(){return title;}

    public void setTitle() {this.title = title;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(des);
    }
}
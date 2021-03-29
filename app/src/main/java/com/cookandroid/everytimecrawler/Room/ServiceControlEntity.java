package com.cookandroid.everytimecrawler.Room;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "controlTable")
public class ServiceControlEntity implements Parcelable {

    //Room에서 자동으로 id를 할당
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String des;
    private String loginId;
    private String loginPw;
    private String cookie_key;
    private String cookie_value;
    private String userAgent;
    private String listnum;


    public ServiceControlEntity(String title, String des) {
        this.title = title;
        this.des = des;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.cookie_key = cookie_key;
        this.cookie_value = cookie_value;
        this.userAgent = userAgent;
        this.listnum = listnum;
    }

    protected ServiceControlEntity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        des = in.readString();
        loginId = in.readString();
        loginPw = in.readString();
        cookie_key = in.readString();
        cookie_value = in.readString();
        userAgent = in.readString();
        listnum = in.readString();
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

    public void setTitle(String title) {this.title = title;}

    public String getLoginId() { return loginId;}

    public void setLoginId(String loginId) { this.loginId = loginId;}

    public String getLoginPw() { return loginPw;}

    public void setLoginPw(String loginPw) { this.loginPw = loginPw;}

    public String getCookie_key() { return cookie_key;}

    public void setCookie_key(String cookie_key) { this.cookie_key = cookie_key;}

    public String getCookie_value() { return cookie_value;}

    public void setCookie_value(String cookie_value) { this.cookie_value = cookie_value;}

    public String getUserAgent() { return userAgent;}

    public void setUserAgent(String userAgent) { this.userAgent = userAgent;}

    public String getListnum() { return listnum;}

    public void setListnum(String listnum) { this.listnum = listnum;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(des);
        dest.writeString(loginId);
        dest.writeString(loginPw);
        dest.writeString(cookie_key);
        dest.writeString(cookie_value);
        dest.writeString(userAgent);
        dest.writeString(listnum);
    }

    @Override
    public String toString() {
        return "ServiceControlEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", des='" + des + '\'' +
                ", loginId='" + loginId + '\'' +
                ", loginPw='" + loginPw + '\'' +
                ", cookie_key='" + cookie_key + '\'' +
                ", cookie_value='" + cookie_value + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", listnum='" + listnum + '\'' +
                '}';
    }
}

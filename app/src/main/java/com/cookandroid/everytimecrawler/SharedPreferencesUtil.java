package com.cookandroid.everytimecrawler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private SharedPreferences pref; // SharedPre~~ 를 생성
    private Context mContext;
    private static final String XML_FILE_NAME = "File Name"; // SharedPreferences xml file name.

    //생성자
    public SharedPreferencesUtil(Context mContext) {
        this.mContext = mContext;
    }

    // 불러오기
    public String getSharedString(String key) {
        pref = mContext.getSharedPreferences(XML_FILE_NAME, Activity.MODE_PRIVATE);
        String json = pref.getString(key, null);
        return json;
    }

    // 저장
    public void setSharedString(String key, String json) {
        pref = mContext.getSharedPreferences(XML_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, json);
        editor.commit();

    }
// 삭제
    public void delShared(String key) {
        pref = mContext.getSharedPreferences(XML_FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

}

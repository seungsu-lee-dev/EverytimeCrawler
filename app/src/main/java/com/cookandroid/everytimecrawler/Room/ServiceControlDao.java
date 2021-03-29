package com.cookandroid.everytimecrawler.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ServiceControlDao {

    @Insert
    void insert(ServiceControlEntity sc);

    @Update
    void update(ServiceControlEntity sc);

    @Delete
    void delete(ServiceControlEntity sc);

    @Query("SELECT * FROM controlTable")
    List<ServiceControlEntity> getAll();

    @Query("DELETE FROM controlTable")
    void  deleteAll();

    @Query("SELECT * FROM controlTable ORDER BY id LIMIT 1")
    LiveData<ServiceControlEntity> loadlastTask();

    @Query("SELECT title FROM controlTable")
    String showTitle();

    @Query("SELECT des FROM controlTable WHERE title = 'check'")
    String showDes();

    @Query("SELECT des FROM controlTable WHERE title = 'loginstate'")
    String showLoginstate();

    @Query("SELECT id FROM controlTable WHERE title = 'check'")
    int showId();

    @Query("SELECT loginId FROM controlTable WHERE title = 'check'")
    String showLoginId();

    @Query("SELECT loginPw FROM controlTable WHERE title = 'check'")
    String showLoginPw();

    @Query("SELECT cookie_key FROM controlTable WHERE title = 'check'")
    String showCookie_key();

    @Query("SELECT cookie_value FROM controlTable WHERE title = 'check'")
    String showCookie_value();

    @Query("SELECT userAgent FROM controlTable WHERE title = 'check'")
    String showUserAgent();

    @Query("SELECT listnum FROM controlTable WHERE title = 'check'")
    String showListnum();
}

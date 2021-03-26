package com.cookandroid.everytimecrawler.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM kTable")
    List<User> getAll();

    @Query("DELETE FROM kTable")
    void  deleteAll();

    @Query("SELECT des FROM kTable")
    String[] showDes();
}

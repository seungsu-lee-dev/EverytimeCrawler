package com.cookandroid.everytimecrawler.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
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
}

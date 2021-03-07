package com.cookandroid.everytimecrawler.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ServiceControlEntity.class}, version = 2, exportSchema = false)
public abstract class ServiceControlDatabase extends RoomDatabase {

    static final Migration MIGRATION_2_3 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public abstract ServiceControlDao ServiceControlDao();
    private static ServiceControlDatabase instance = null;


    //싱글톤
    //  error: AppDatabase() has private access in AppDatabase
    // 추상클래스에서 생성자를 private 하게 만들어 줄 필요가없음
    // 생성하면 위에와 같은 에러가 발생함
    /*private AppDatabase() {   }*/
    //데이터에 수정 하고싶을때에는 version을 하나 올리고, fallbackToDestructiveMigration 사용해야함

    public static synchronized ServiceControlDatabase getInstance(Context context){
        if(instance == null){
            instance =  Room.databaseBuilder(context.getApplicationContext(),
                    ServiceControlDatabase.class, "control_Database.db")
                    .createFromAsset("database/control_Database.db")
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return instance;
    }
}

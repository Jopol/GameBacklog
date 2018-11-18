package com.example.joel.gamebacklog;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {GameObj.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract GameDAO gameDao();

    private final static String NAME_DATABASE = "games_db";

    //Static instance
    private static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDataBase.class, NAME_DATABASE)
                    .build();
            //.allowMainThreadQueries()
        }
        return sInstance;
    }
}

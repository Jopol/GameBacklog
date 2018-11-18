package com.example.joel.gamebacklog;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM gameobj")
    List<GameObj> getAllGames();

    @Insert
    void insertGames(GameObj games);


    @Delete
    void deleteGames(GameObj games);


    @Update
    void updateGames(GameObj games);

}


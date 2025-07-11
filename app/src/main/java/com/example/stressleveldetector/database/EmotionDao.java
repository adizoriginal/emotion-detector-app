package com.example.stressleveldetector.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.stressleveldetector.model.EmotionResult;

import java.util.List;

@Dao
public interface EmotionDao {
    @Insert
    void insert(EmotionResult result);

    @Query("SELECT * FROM EmotionResult ORDER BY timestamp DESC")
    List<EmotionResult> getAllResults();
}

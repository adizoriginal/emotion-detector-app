package com.example.stressleveldetector.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.stressleveldetector.model.EmotionResult;

@Database(entities = {EmotionResult.class}, version = 1)
public abstract class EmotionDatabaseHelper extends RoomDatabase {
    private static EmotionDatabaseHelper instance;

    public abstract EmotionDao emotionDao();

    public static synchronized EmotionDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            EmotionDatabaseHelper.class, "emotion_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

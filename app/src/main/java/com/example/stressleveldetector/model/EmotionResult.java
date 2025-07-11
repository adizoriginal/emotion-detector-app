package com.example.stressleveldetector.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EmotionResult {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String emotion;
    public long timestamp;

    public EmotionResult(String emotion, long timestamp) {
        this.emotion = emotion;
        this.timestamp = timestamp;
    }
}

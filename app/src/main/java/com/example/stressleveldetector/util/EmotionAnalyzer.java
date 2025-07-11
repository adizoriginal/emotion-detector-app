package com.example.stressleveldetector.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.stressleveldetector.database.EmotionDatabaseHelper;
import com.example.stressleveldetector.model.EmotionResult;

public class EmotionAnalyzer {

    public static void analyzeEmotion(Bitmap photo, Context context) {
        EmotionDetector detector = new EmotionDetector(context);

        detector.detectEmotion(photo, new EmotionDetector.EmotionCallback() {
            @Override
            public void onEmotionDetected(String emotion) {
                // Save result to Room DB
                EmotionResult result = new EmotionResult(emotion, System.currentTimeMillis());
                new Thread(() -> EmotionDatabaseHelper.getInstance(context).emotionDao().insert(result)).start();


            }
        });
    }
}

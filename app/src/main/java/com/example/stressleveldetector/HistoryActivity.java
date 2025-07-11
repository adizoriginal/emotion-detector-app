package com.example.stressleveldetector;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stressleveldetector.database.EmotionDatabaseHelper;
import com.example.stressleveldetector.model.EmotionResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);

        // Load from database
        new Thread(() -> {
            List<EmotionResult> results = EmotionDatabaseHelper.getInstance(this)
                    .emotionDao().getAllResults();

            List<String> formatted = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

            for (EmotionResult result : results) {
                String line = result.emotion + " - " + formatter.format(result.timestamp);
                formatted.add(line);
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        formatted
                );
                historyListView.setAdapter(adapter);
            });
        }).start();
    }
}

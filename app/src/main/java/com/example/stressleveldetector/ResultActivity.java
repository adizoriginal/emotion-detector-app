package com.example.stressleveldetector;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    private TextView emotionTextView, suggestionTextView;
    private MediaPlayer mediaPlayer;
    private LinearLayout resultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultLayout = findViewById(R.id.resultLayout);
        emotionTextView = findViewById(R.id.emotionTextView);
        suggestionTextView = findViewById(R.id.suggestionTextView);

        String emotion = getIntent().getStringExtra("emotion");
        emotionTextView.setText("Detected Emotion: " + emotion);

        String suggestion = getRandomSuggestion(emotion);
        suggestionTextView.setText(suggestion);

        playFadeInAnimation();
        applyEmotionColor(emotion);

        if (emotion.equalsIgnoreCase("angry") ||
                emotion.equalsIgnoreCase("sad") ||
                emotion.equalsIgnoreCase("surprised") ||
                emotion.equalsIgnoreCase("afraid") ||
                emotion.equalsIgnoreCase("disgusted")) {

            int[] relaxingTracks = {
                    R.raw.relaxing_music,
                    R.raw.relaxing_music1,
                    R.raw.relaxing_music2
            };

            int randomIndex = new Random().nextInt(relaxingTracks.length);
            mediaPlayer = MediaPlayer.create(this, relaxingTracks[randomIndex]);
            mediaPlayer.start();
        }
    }

    private void playFadeInAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        resultLayout.startAnimation(fadeIn);
    }

    private void applyEmotionColor(String emotion) {
        int colorResId;
        switch (emotion.toLowerCase()) {
            case "angry":
                colorResId = R.color.emotion_angry;
                break;
            case "sad":
                colorResId = R.color.emotion_sad;
                break;
            case "happy":
                colorResId = R.color.emotion_happy;
                break;
            case "surprised":
                colorResId = R.color.emotion_surprised;
                break;
            case "afraid":
                colorResId = R.color.emotion_afraid;
                break;
            case "disgusted":
                colorResId = R.color.emotion_disgusted;
                break;
            default:
                colorResId = R.color.emotion_neutral;
                break;
        }

        int color = ContextCompat.getColor(this, colorResId);
        resultLayout.setBackgroundColor(color);
    }

    private String getRandomSuggestion(String emotion) {
        List<String> quotes;

        switch (emotion.toLowerCase()) {
            case "angry":
                quotes = Arrays.asList("Take a deep breath.", "Step away for a moment.", "Control what you can.");
                break;
            case "sad":
                quotes = Arrays.asList("It’s okay to cry.", "You are not alone.", "Better days are ahead.");
                break;
            case "surprised":
                quotes = Arrays.asList("Keep exploring!", "Life’s full of surprises.");
                break;
            case "happy":
                quotes = Arrays.asList("Stay positive!", "Keep smiling!", "Your joy is infectious!");
                break;
            case "afraid":
                quotes = Arrays.asList("You are brave.", "Face fear step by step.");
                break;
            case "disgusted":
                quotes = Arrays.asList("Take a breather.", "Let go of the negativity.");
                break;
            default:
                quotes = Arrays.asList("Stay grounded.", "Be present and breathe.");
        }

        return quotes.get(new Random().nextInt(quotes.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}

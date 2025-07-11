package com.example.stressleveldetector.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.stressleveldetector.database.EmotionDatabaseHelper;
import com.example.stressleveldetector.model.EmotionResult;
import com.example.stressleveldetector.model.FacePlusPlusResponse;
import com.example.stressleveldetector.network.FacePlusPlusApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmotionDetector {

    private static final String TAG = "EmotionDetector";

    private final FacePlusPlusApi api;
    private final Context context;

    private final String apiKey = "nd0nGAOKKcHKxA3YCDUdxd_ONxNhlK_u";
    private final String apiSecret = "jhzBAWK36_4KLlJa_UP6f3E1r9tGXHbJ";

    public EmotionDetector(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-us.faceplusplus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(FacePlusPlusApi.class);
    }

    public void detectEmotion(Bitmap bitmap, EmotionCallback callback) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            Log.e(TAG, "Invalid bitmap passed to detectEmotion");
            callback.onEmotionDetected("error");
            return;
        }

        try {
            File imageFile = createTempFile(bitmap);

            if (!imageFile.exists() || imageFile.length() == 0) {
                Log.e(TAG, "Temporary image file not created or is empty");
                callback.onEmotionDetected("error");
                return;
            }

            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), apiKey);
            RequestBody secret = RequestBody.create(MediaType.parse("text/plain"), apiSecret);
            RequestBody returnAttr = RequestBody.create(MediaType.parse("text/plain"), "emotion");

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image_file", imageFile.getName(), reqFile);

            Call<FacePlusPlusResponse> call = api.detectEmotion(key, secret, returnAttr, body);
            call.enqueue(new Callback<FacePlusPlusResponse>() {
                @Override
                public void onResponse(@NonNull Call<FacePlusPlusResponse> call, @NonNull Response<FacePlusPlusResponse> response) {
                    String resultEmotion = "unknown";

                    try {
                        if (response.isSuccessful() && response.body() != null && !response.body().faces.isEmpty()) {
                            FacePlusPlusResponse.Emotion emotion = response.body().faces.get(0).attributes.emotion;
                            resultEmotion = getDominantEmotion(emotion);
                        } else {
                            Log.w(TAG, "No face detected or response body was null");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception while parsing response", e);
                    }

                    // Save result to Room DB
                    EmotionResult result = new EmotionResult(resultEmotion, System.currentTimeMillis());
                    new Thread(() -> EmotionDatabaseHelper.getInstance(context).emotionDao().insert(result)).start();

                    callback.onEmotionDetected(resultEmotion);
                }

                @Override
                public void onFailure(@NonNull Call<FacePlusPlusResponse> call, @NonNull Throwable t) {
                    Log.e(TAG, "Emotion detection failed: " + t.getMessage(), t);
                    callback.onEmotionDetected("error");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Failed during emotion detection setup", e);
            callback.onEmotionDetected("error");
        }
    }

    private File createTempFile(Bitmap bitmap) throws IOException {
        File file = File.createTempFile("temp", ".jpg", context.getCacheDir());
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        }
        return file;
    }

    private String getDominantEmotion(FacePlusPlusResponse.Emotion e) {
        float maxVal = -1;
        String dominant = "neutral";

        if (e.anger > maxVal) { maxVal = e.anger; dominant = "angry"; }
        if (e.happiness > maxVal) { maxVal = e.happiness; dominant = "happy"; }
        if (e.sadness > maxVal) { maxVal = e.sadness; dominant = "sad"; }
        if (e.surprise > maxVal) { maxVal = e.surprise; dominant = "surprised"; }
        if (e.fear > maxVal) { maxVal = e.fear; dominant = "afraid"; }
        if (e.disgust > maxVal) { maxVal = e.disgust; dominant = "disgusted"; }
        if (e.neutral > maxVal) { maxVal = e.neutral; dominant = "neutral"; }

        return dominant;
    }

    public interface EmotionCallback {
        void onEmotionDetected(String emotion);
    }
}

package com.example.stressleveldetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.stressleveldetector.util.EmotionDetector;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button selectPhotoBtn, captureBtn, analyzeBtn, historyBtn;
    private Bitmap selectedBitmap;
    private EmotionDetector emotionDetector;

    // Permissions
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        selectPhotoBtn = findViewById(R.id.selectPhotoButton);
        captureBtn = findViewById(R.id.captureButton);
        analyzeBtn = findViewById(R.id.analyzeButton);
        historyBtn = findViewById(R.id.historyButton);

        // Initialize emotion detector
        emotionDetector = new EmotionDetector(this);

        selectPhotoBtn.setOnClickListener(v -> selectPhotoFromGallery());
        captureBtn.setOnClickListener(v -> checkCameraPermissionAndCapture());

        analyzeBtn.setOnClickListener(v -> {
            if (selectedBitmap != null) {
                Toast.makeText(this, "Analyzing photo...", Toast.LENGTH_SHORT).show();
                emotionDetector.detectEmotion(selectedBitmap, emotion -> {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("emotion", emotion);
                    startActivity(intent);
                });
            } else {
                Toast.makeText(this, "Please select or capture a photo first", Toast.LENGTH_SHORT).show();
            }
        });

        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // Runtime permission check before launching camera
    private void checkCameraPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            capturePhoto();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Gallery photo picker
    private final ActivityResultLauncher<Intent> photoPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        selectedBitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(selectedBitmap);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Camera launcher
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.containsKey("data")) {
                        selectedBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(selectedBitmap);
                    } else {
                        Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Launch gallery
    private void selectPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    // Launch camera
    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }
}

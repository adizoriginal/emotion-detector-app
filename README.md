# Emotion-Based Sound Player (Android)

This is an Android app that detects human emotions from facial images using the Face++ API. It allows users to either capture a photo or select one from their gallery, analyzes the image to detect the emotional state of the person, and then plays a sound clip that corresponds to the detected emotion.

## 🎯 Features

- Detects facial emotions using the **Face++ Emotion Recognition API**
- Supports both **camera capture** and **gallery image upload**
- Plays a predefined **sound clip** for each detected emotion
- Simple and clean user interface built with Android Studio

## 😃 Supported Emotions and Sounds

| Emotion    | Sound Played       |
|------------|--------------------|
| Angry      | Angry sound clip   |
| Sad        | Sad sound clip     |
| Surprised  | Surprised sound    |
| Afraid     | Afraid sound       |
| Neutral / Happy / Others | No sound or neutral tone |

## 🛠️ Tech Stack

- Language: Java (or Kotlin if applicable)
- IDE: Android Studio
- Emotion Detection API: [Face++](https://www.faceplusplus.com/)
- Media Playback: Android `MediaPlayer`
- Permissions: Camera, Internet, and Storage

## 📦 How to Build and Run

1. Clone the repository:

   git clone https://github.com/adizoriginal/emotion-detector-app.git

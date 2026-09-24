English | [简体中文](README.md)

# Krabo - Air Pistol for Android Watches

Krabo is a minimalist air pistol application specifically designed for legacy Android watches running Android 5.0 and above. Inspired by the viral "Crab acto", it aims to fill the gap for interactive apps on low-spec Android wearables.

## Key Selling Points

- **Built for Legacy Devices**: Supports Android 5.0+ (API 21), perfectly optimized for low-performance and low-memory Android watches.
- **Minimalist Design**: Features a pure black background with white text. No redundant elements, ensuring clarity on small watch screens.
- **Dual Trigger Mechanisms**: Play sound effects either by tapping the screen or swinging your fist forward (accelerometer-based).
- **Dual Character Switch**: Switch characters seamlessly using the left and right arrows on the UI. Each character has unique images and sound effects.
- **Deep Optimization for Old Hardware**:
  - Fixed the legacy SoundPool volume bug on old systems by utilizing MediaPlayer to read system volume in real-time.
  - Built-in 400ms ANR (Application Not Responding) prevention lock to prevent high-frequency sensor callbacks from freezing low-performance watches.

## How to Build

### Prerequisites
- Android Studio (Latest version recommended)
- Java 8 or higher
- Android SDK (Minimum API 21)

### Build Steps

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/Krabo.git
```
2. Open the project in Android Studio and wait for Gradle synchronization to complete.
3. Prepare the required resource files (see below).
4. Connect your Android watch or start an emulator running API 21 or higher.
5. Click the Run button in Android Studio to install the app on your watch.

Resource File Configuration (Required)

Due to copyright reasons, this repository does not include the original audio and image resources. If you compile and run without adding these files, the app will crash or play no sound when triggered.

Please add your own files in the following directories:

· Audio Files: Place in app/src/main/res/raw/ folder, named:
  · role1.wav
  · role2.wav
· Character Images: Place in app/src/main/res/drawable/ folder, named:
  · role1.png
  · role2.png 
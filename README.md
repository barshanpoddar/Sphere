# Sphere

A modern Android video streaming application built with Jetpack Compose, providing an intuitive and engaging user experience for discovering and watching videos.

## Features

- **Home**: Browse featured and recommended videos
- **Explore**: Discover trending and popular content
- **Search**: Find videos with powerful search functionality
- **Subscription**: Manage your subscribed channels
- **Music**: Access music videos and audio content
- **Video Player**: High-quality video playback with ExoPlayer

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Compose
- **Navigation**: Navigation Compose
- **Video Playback**: Android Media3 (ExoPlayer)
- **Image Loading**: Coil
- **Build System**: Gradle with Kotlin DSL

## Requirements

- Android Studio Iguana (2023.2.1) or later
- Minimum Android SDK: API 24 (Android 7.0)
- Target Android SDK: API 36 (Android 16)
- Kotlin 1.9+

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/Sphere.git
   cd Sphere
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Build the project**:
   - Wait for Gradle sync to complete
   - Build → Make Project (Ctrl+F9)

4. **Run on device/emulator**:
   - Connect an Android device or start an emulator
   - Run → Run 'app' (Shift+F10)

## Project Structure

```
app/
├── src/main/java/com/sphere/app/
│   ├── MainActivity.kt          # Main activity entry point
│   ├── SphereApp.kt             # Main app composable with navigation
│   ├── Screen.kt                # Navigation screen definitions
│   └── ui/
│       ├── screens/             # UI screens (Home, Search, etc.)
│       └── theme/               # App theming
└── src/main/res/                # Android resources
```

## Contributing

This project is proprietary and not open for external contributions.

## License

This project is proprietary software. All rights reserved.

See [LICENSE](LICENSE) for more details.
This repository serves as the course material for the [Android Media Playback with Media3 ExoPlayer](https://www.udemy.com/course/android-media-playback-with-media3-exoplayer/?referralCode=0DCF19B171BCEE1B0BFB) course.

To check out the revision used in the course:
```
git clone git@github.com:composexy/Movies.git
git checkout v1.0
```

This repository contains two modules:

app module: A sample app demonstrating media playback using ExoPlayer and Jetpack Compose.
labs module: A demo app that showcases various use cases with ExoPlayer:

[`DrmContentTests.kt`](labs/src/main/java/com/media3/movies/DrmContentTests.kt): Comparison between TextureView and SurfaceView for DRM content playback.
[`HeavyWorkTests.kt`](labs/src/main/java/com/media3/movies/HeavyWorkTests.kt): Comparison between TextureView and SurfaceView with simulated heavy work on the UI thread.
[`OffsetTests.kt`](labs/src/main/java/com/media3/movies/OffsetTests.kt): Comparison between TextureView and SurfaceView for video position transitions.
[`RotationTests.kt`](labs/src/main/java/com/media3/movies/RotationTests.kt): Comparison between TextureView and SurfaceView for video rotation.
[`ScaleTests.kt`](labs/src/main/java/com/media3/movies/ScaleTests.kt): Comparison between TextureView and SurfaceView for video scaling.
[`ShakaPlayerTests.kt`](labs/src/main/java/com/media3/movies/ShakaPlayerTests.kt): A memory usage demo comparing ExoPlayer with a WebView-wrapped Shaka player.
[`SplitViewTests.kt`](labs/src/main/java/com/media3/movies/SplitViewTests.kt): A code challenge to run two players side by side on Android devices.
[`TransparencyTests.kt`](labs/src/main/java/com/media3/movies/TransparencyTests.kt): Comparison between TextureView and SurfaceView for applying video transparency.

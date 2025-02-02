package com.media3.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SurfaceViewWithHeavyWork() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
        useSurfaceView = true,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            Thread.sleep(250)
        }
    }
}

@Composable
fun TextureViewWithHeavyWork() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
        useSurfaceView = false,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            Thread.sleep(250)
        }
    }
}
package com.media3.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun SurfaceViewWithScale() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).scale(0.5f),
        useSurfaceView = true,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}

@Composable
fun TextureViewWithScale() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).scale(0.5f),
        useSurfaceView = false,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}
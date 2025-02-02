package com.media3.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Composable
fun SurfaceViewWithOffset() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).offset {
            IntOffset(0, 400)
        },
        useSurfaceView = true,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}

@Composable
fun TextureViewWithOffset() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).offset {
            IntOffset(0, 400)
        },
        useSurfaceView = false,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}
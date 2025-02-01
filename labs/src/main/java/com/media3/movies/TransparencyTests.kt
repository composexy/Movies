package com.media3.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun SurfaceViewWithTransparency() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).alpha(0.3f),
        useSurfaceView = true,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}

@Composable
fun TextureViewWithTransparency() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).alpha(0.3f),
        useSurfaceView = false,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.clearStreamUrl
        )
    )
}

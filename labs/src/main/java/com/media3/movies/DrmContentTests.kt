package com.media3.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SecureContentWithSurfaceView() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
        useSurfaceView = true,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.secureStreamUrl,
            licenseUrl = StreamUrls.drmLicenseUrl
        )
    )
}

@Composable
fun SecureContentWithTextureView() {
    VideoSurface(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
        useSurfaceView = false,
        playerConfig = PlayerConfig(
            streamUrl = StreamUrls.secureStreamUrl,
            licenseUrl = StreamUrls.drmLicenseUrl
        )
    )
}

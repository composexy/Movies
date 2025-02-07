package com.media3.movies

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

@Composable
fun SplitViewWithClearContent() {
    val activity = LocalActivity.current
    Row {
        VideoSurface(
            modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).weight(1.0f),
            useSurfaceView = true,
            playerConfig = PlayerConfig(
                streamUrl = StreamUrls.clearStreamUrl,
                minBufferSize = 15_000,
                maxBufferSize = 15_000,
                maxVideoHeight = 720
            )
        )
        VideoSurface(
            modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).weight(1.0f),
            useSurfaceView = true,
            playerConfig = PlayerConfig(
                streamUrl = StreamUrls.clearStreamUrl,
                minBufferSize = 15_000,
                maxBufferSize = 15_000,
                maxVideoHeight = 720
            )
        )
    }
    SideEffect {
        activity?.let {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}
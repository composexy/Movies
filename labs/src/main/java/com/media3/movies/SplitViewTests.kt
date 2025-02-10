package com.media3.movies

import android.content.pm.ActivityInfo
import android.media.MediaCodecList
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

fun maxSecureH264DecoderInstance(): Int {
    val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
    val codecInfos = codecList.codecInfos
    val matchingDecoders = codecInfos.filter { info ->
        val isSecure = info.name.contains("secure", ignoreCase = true)
        val isH264 = info.name.contains("avc", ignoreCase = true) ||
                info.name.contains("h264", ignoreCase = true)
        isSecure && isH264 && !info.isEncoder
    }
    return matchingDecoders.firstOrNull()?.getCapabilitiesForType("video/avc")?.maxSupportedInstances ?: 1
}

@Composable
fun SplitViewWithSecureContent() {
    val activity = LocalActivity.current
    Row {
        val maxSecureDecoderInstance = remember { maxSecureH264DecoderInstance() }
        if (maxSecureDecoderInstance > 1) {
            VideoSurface(
                modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).weight(1.0f),
                useSurfaceView = true,
                playerConfig = PlayerConfig(
                    streamUrl = StreamUrls.secureStreamUrl,
                    licenseUrl = StreamUrls.drmLicenseUrl,
                    minBufferSize = 15_000,
                    maxBufferSize = 15_000,
                    maxVideoHeight = 720
                )
            )
            VideoSurface(
                modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f).weight(1.0f),
                useSurfaceView = true,
                playerConfig = PlayerConfig(
                    streamUrl = StreamUrls.secureStreamUrl,
                    licenseUrl = StreamUrls.drmLicenseUrl,
                    minBufferSize = 15_000,
                    maxBufferSize = 15_000,
                    maxVideoHeight = 720
                )
            )
        } else {
            VideoSurface(
                modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
                useSurfaceView = true,
                playerConfig = PlayerConfig(
                    streamUrl = StreamUrls.secureStreamUrl,
                    licenseUrl = StreamUrls.drmLicenseUrl,
                    minBufferSize = 15_000,
                    maxBufferSize = 15_000,
                    maxVideoHeight = 720
                )
            )
        }
    }
    SideEffect {
        activity?.let {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}

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
package com.media3.movies

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.DrmConfiguration
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
import androidx.media3.exoplayer.DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS
import androidx.media3.exoplayer.ExoPlayer

object StreamUrls {
    const val clearStreamUrl =
        "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
    const val secureStreamUrl =
        "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd"
    const val drmLicenseUrl =
        "https://proxy.uat.widevine.com/proxy?video_id=GTS_HW_SECURE_ALL&provider=widevine_test"
}

@OptIn(UnstableApi::class)
class PlayerConfig(
    val streamUrl: String = StreamUrls.clearStreamUrl,
    val licenseUrl: String? = null,
    val minBufferSize: Int = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
    val maxBufferSize: Int = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
    val maxVideoHeight: Int = Int.MAX_VALUE,
)

@OptIn(UnstableApi::class)
@Composable
fun VideoSurface(
    modifier: Modifier = Modifier,
    useSurfaceView: Boolean = true,
    playerConfig: PlayerConfig,
    player: ExoPlayer = rememberExoPlayer(playerConfig),
) {
    if (useSurfaceView) {
        AndroidExternalSurface(modifier = modifier) {
            onSurface { surface, _, _ ->
                player.setVideoSurface(surface)
                surface.onDestroyed {
                    player.setVideoSurface(null)
                }
            }
        }
    } else {
        AndroidEmbeddedExternalSurface(modifier = modifier) {
            onSurface { surface, _, _ ->
                player.setVideoSurface(surface)
                surface.onDestroyed {
                    player.setVideoSurface(null)
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(config: PlayerConfig): ExoPlayer {
    val context = LocalContext.current
    return remember {
        val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            config.minBufferSize,
            config.maxBufferSize,
            DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
        ).build()
        val videoTrackSelectionParameters = TrackSelectionParameters.Builder(context)
            .setMaxVideoSize(Int.MAX_VALUE, config.maxVideoHeight)
            .build()
        val mediaItemBuilder = MediaItem.Builder().apply {
            setUri(Uri.parse(config.streamUrl))
            config.licenseUrl?.let { licenseUrl ->
                setDrmConfiguration(
                    DrmConfiguration.Builder(C.WIDEVINE_UUID)
                        .setLicenseUri(licenseUrl)
                        .build()
                )
            }
        }
        ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build().apply {
                trackSelectionParameters = videoTrackSelectionParameters
                setMediaItem(mediaItemBuilder.build())
                prepare()
                play()
            }
    }
}

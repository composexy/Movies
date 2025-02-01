package com.media3.movies

import android.net.Uri
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.DrmConfiguration
import androidx.media3.exoplayer.ExoPlayer

object StreamUrls {
    const val clearStreamUrl =
        "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
    const val secureStreamUrl =
        "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd"
    const val drmLicenseUrl =
        "https://proxy.uat.widevine.com/proxy?video_id=GTS_HW_SECURE_ALL&provider=widevine_test"
}

class PlayerConfig(
    val streamUrl: String = StreamUrls.clearStreamUrl,
    val licenseUrl: String? = null
)

@Composable
fun VideoSurface(
    modifier: Modifier = Modifier,
    useSurfaceView: Boolean = true,
    playerConfig: PlayerConfig,
    player: ExoPlayer = rememberExoPlayer(playerConfig)
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

@Composable
fun rememberExoPlayer(config: PlayerConfig): ExoPlayer {
    val context = LocalContext.current
    return remember {
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
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItemBuilder.build())
            prepare()
            play()
        }
    }
}

package com.media3.movies.playback

import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun VideoSurface(
    modifier: Modifier = Modifier,
    player: ExoPlayer = rememberExoPlayer()
) {
    AndroidExternalSurface(modifier = modifier) {
        onSurface { surface, _, _ ->
            player.setVideoSurface(surface)
            surface.onDestroyed {
                player.setVideoSurface(null)
            }
        }
    }
}

@Composable
fun rememberExoPlayer(): ExoPlayer {
    val streamUrl = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
    val context = LocalContext.current
    return remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(streamUrl))
            prepare()
            play()
        }
    }
}

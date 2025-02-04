package com.media3.movies.playback

import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {
    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    AndroidExternalSurface(
        modifier = modifier.aspectRatio(playerUiModel.videoAspectRatio)
    ) {
        onSurface { surface, _, _ ->
            playerViewModel.setSurface(surface)
            surface.onDestroyed {
                playerViewModel.clearSurface()
            }
        }
    }
}

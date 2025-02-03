package com.media3.movies.playback

import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {
    AndroidExternalSurface(modifier = modifier) {
        onSurface { surface, _, _ ->
            playerViewModel.setSurface(surface)
            surface.onDestroyed {
                playerViewModel.clearSurface()
            }
        }
    }
}

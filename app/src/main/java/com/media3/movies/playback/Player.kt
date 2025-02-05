package com.media3.movies.playback

import androidx.annotation.DrawableRes
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.media3.movies.R

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {
    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    Box {
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

        VideoOverlay(
            modifier = Modifier.matchParentSize(),
            playerViewModel = playerViewModel
        )
    }
}

@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {
    Box(
        modifier = modifier
    ) {
        PlaybackControls(
            modifier = Modifier.matchParentSize(),
            onCollapseClicked = {},
            onExpandClicked = {}
        )
    }
}

@Composable
fun PlaybackControls(
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false,
    onCollapseClicked: () -> Unit,
    onExpandClicked: () -> Unit
) {
    Box(
        modifier = modifier.background(Color(0xA0000000))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (isFullScreen) {
                PlaybackButton(
                    R.drawable.collapse,
                    description = "Exit full screen"
                ) {
                    onCollapseClicked()
                }
            } else {
                PlaybackButton(
                    R.drawable.expand,
                    description = "Enter full screen"
                ) {
                    onExpandClicked()
                }
            }
        }
    }
}

@Composable
fun PlaybackButton(
    @DrawableRes resourceId: Int,
    description: String,
    onClick: () -> Unit = {}
) {
    Image(
        modifier = Modifier.size(32.dp).clickable(onClick = onClick),
        contentDescription = description,
        painter = painterResource(resourceId)
    )
}
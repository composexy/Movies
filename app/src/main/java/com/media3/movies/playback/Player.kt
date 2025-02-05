package com.media3.movies.playback

import androidx.annotation.DrawableRes
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
                .clickable {
                    playerViewModel.showPlayerControls()
                }
        ) {
            onSurface { surface, _, _ ->
                playerViewModel.handleAction(AttachSurface(surface))
                surface.onDestroyed {
                    playerViewModel.handleAction(DetachSurface)
                }
            }
        }

        VideoOverlay(
            modifier = Modifier.matchParentSize(),
            playerViewModel = playerViewModel,
            onCollapseClicked = {
                playerViewModel.exitFullScreen()
            },
            onExpandClicked = {
                playerViewModel.enterFullScreen()
            },
            onControlsClicked = {
                playerViewModel.hidePlayerControls()
            },
            onAction = {
                playerViewModel.handleAction(action = it)
            }
        )
    }
}

@Composable
fun VideoOverlay(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel,
    onCollapseClicked: () -> Unit,
    onExpandClicked: () -> Unit,
    onControlsClicked: () -> Unit,
    onAction: (Action) -> Unit
) {
    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    Box(
        modifier = modifier
    ) {
        if (playerUiModel.playerControlsVisible) {
            PlaybackControls(
                modifier = Modifier.matchParentSize().clickable(onClick = onControlsClicked),
                isFullScreen = playerUiModel.isFullScreen,
                playerUiModel = playerUiModel,
                onCollapseClicked = onCollapseClicked,
                onExpandClicked = onExpandClicked,
                onAction = onAction,
            )
        }
    }
}

@Composable
fun PlaybackControls(
    modifier: Modifier = Modifier,
    playerUiModel: PlayerUiModel,
    isFullScreen: Boolean,
    onCollapseClicked: () -> Unit,
    onExpandClicked: () -> Unit,
    onAction: (Action) -> Unit
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
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (playerUiModel.playbackState.isReady()) {
                PlaybackButton(
                    R.drawable.startover,
                    description = "Start over"
                ) {
                    onAction(Seek(0))
                }
                PlaybackButton(
                    R.drawable.rewind,
                    description = "Rewind"
                ) {
                    onAction(Rewind(15_000))
                }
            }
            when (playerUiModel.playbackState) {
                PlaybackState.PLAYING -> {
                    PlaybackButton(
                        R.drawable.pause,
                        description = "Pause"
                    ) {
                        onAction(Pause)
                    }
                }
                PlaybackState.PAUSED -> {
                    PlaybackButton(
                        R.drawable.play,
                        description = "Play"
                    ) {
                        onAction(Resume)
                    }
                }
                PlaybackState.IDLE -> {
                    PlaybackButton(
                        R.drawable.play,
                        description = "Start"
                    ) {
                        onAction(Start())
                    }
                }
                PlaybackState.BUFFERING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Color.White
                    )
                }
                PlaybackState.COMPLETED -> {
                    PlaybackButton(
                        R.drawable.replay,
                        description = "Replay"
                    ) {
                        onAction(Start(0))
                    }
                }
                PlaybackState.ERROR -> {
                    PlaybackButton(
                        R.drawable.error,
                        description = "Error"
                    )
                    PlaybackButton(
                        R.drawable.replay,
                        description = "Retry"
                    ) {
                        // TODO: Pass in current position later
                        onAction(Start(0))
                    }
                }
            }
            if (playerUiModel.playbackState.isReady()) {
                PlaybackButton(
                    R.drawable.fastforward,
                    description = "Fast forward"
                ) {
                    onAction(FastForward(15_000))
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
package com.media3.movies.playback

import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.TimeBar
import com.media3.movies.CoverImage
import com.media3.movies.R
import com.media3.movies.Utils

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel,
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
            },
            onSettingsClicked = {
                playerViewModel.openTrackSelector()
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
    onSettingsClicked: () -> Unit,
    onAction: (Action) -> Unit,
) {
    val playerUiModel by playerViewModel.playerUiModel.collectAsState()

    Box(
        modifier = modifier
    ) {
        playerUiModel.placeHolderImageResourceId?.let {
            CoverImage(
                modifier = Modifier.matchParentSize(),
                imageRes = it
            )
        }
        if (playerUiModel.playerControlsVisible) {
            PlaybackControls(
                modifier = Modifier.matchParentSize().clickable(onClick = onControlsClicked),
                isFullScreen = playerUiModel.isFullScreen,
                playerUiModel = playerUiModel,
                onCollapseClicked = onCollapseClicked,
                onExpandClicked = onExpandClicked,
                onSettingsClicked = onSettingsClicked,
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
    onSettingsClicked: () -> Unit,
    onAction: (Action) -> Unit,
) {
    Box(
        modifier = modifier.background(Color(0xA0000000))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PlaybackButton(
                R.drawable.settings,
                description = "Open track selector"
            ) {
                onSettingsClicked()
            }
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
                        onAction(Start(playerUiModel.timelineUiModel?.currentPositionInMs))
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
        playerUiModel.timelineUiModel?.let { timeline ->
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                PlaybackPosition(
                    contentPositionInMs = timeline.currentPositionInMs,
                    contentDurationInMs = timeline.durationInMs
                )
                TimeBar(
                    positionInMs = timeline.currentPositionInMs,
                    durationInMs = timeline.durationInMs,
                    bufferedPositionInMs = timeline.bufferedPositionInMs
                ) {
                    onAction(Seek(it.toLong()))
                }
            }
        }
    }
}

private enum class TrackState {
    VIDEO, AUDIO, SUBTITLE, LIST
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackSelector(
    trackSelectionUiModel: TrackSelectionUiModel,
    onVideoTrackSelected: (VideoTrack) -> Unit,
    onAudioTrackSelected: (AudioTrack) -> Unit,
    onSubtitleTrackSelected: (SubtitleTrack) -> Unit,
    onDismiss: () -> Unit
) {
    var currentState by remember { mutableStateOf(TrackState.LIST) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        when (currentState) {
            TrackState.LIST -> {
                Column {
                    Text(
                        text = "Video Tracks",
                        modifier = Modifier.clickable {
                            currentState = TrackState.VIDEO
                        }.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Text(
                        text = "Audio Tracks",
                        modifier = Modifier.clickable {
                            currentState = TrackState.AUDIO
                        }.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Text(
                        text = "Subtitle Tracks",
                        modifier = Modifier.clickable {
                            currentState = TrackState.SUBTITLE
                        }.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            TrackState.VIDEO -> {
                Column {
                    trackSelectionUiModel.videoTracks.forEach { videoTrack ->
                        Text(
                            modifier = Modifier.clickable {
                                onVideoTrackSelected(videoTrack)
                                onDismiss()
                            }.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = videoTrack.displayName,
                            color = if (videoTrack === trackSelectionUiModel.selectedVideoTrack) {
                                Color.Yellow
                            } else {
                                Color.White
                            }
                        )
                    }
                }
            }
            TrackState.AUDIO -> {
                Column {
                    trackSelectionUiModel.audioTracks.forEach { audioTrack ->
                        Text(
                            modifier = Modifier.clickable {
                                onAudioTrackSelected(audioTrack)
                                onDismiss()
                            }.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = audioTrack.displayName,
                            color = if (audioTrack === trackSelectionUiModel.selectedAudioTrack) {
                                Color.Yellow
                            } else {
                                Color.White
                            }
                        )
                    }
                }
            }
            TrackState.SUBTITLE -> {
                Column {
                    trackSelectionUiModel.subtitleTracks.forEach { subtitleTrack ->
                        Text(
                            modifier = Modifier.clickable {
                                onSubtitleTrackSelected(subtitleTrack)
                                onDismiss()
                            }.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = subtitleTrack.displayName,
                            color = if (subtitleTrack === trackSelectionUiModel.selectedSubtitleTrack) {
                                Color.Yellow
                            } else {
                                Color.White
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaybackPosition(
    contentPositionInMs: Long,
    contentDurationInMs: Long,
) {
    val positionString = Utils.formatMsToString(contentPositionInMs)
    val durationString = Utils.formatMsToString(contentDurationInMs)
    Text(
        text = "$positionString / $durationString",
        fontSize = 10.sp
    )
}

@OptIn(UnstableApi::class)
@Composable
fun TimeBar(
    positionInMs: Long,
    durationInMs: Long,
    bufferedPositionInMs: Long,
    onSeek: (Float) -> Unit,
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        factory = { context ->
            DefaultTimeBar(context).apply {
                setScrubberColor(0xFFFF0000.toInt())
                setPlayedColor(0xCCFF0000.toInt())
                setBufferedColor(0x77FF0000)
            }
        },
        update = { timeBar ->
            with(timeBar) {
                addListener(object : TimeBar.OnScrubListener {
                    override fun onScrubStart(timeBar: TimeBar, position: Long) {}
                    override fun onScrubMove(timeBar: TimeBar, position: Long) {}
                    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                        onSeek(position.toFloat())
                    }
                })
                setDuration(durationInMs)
                setPosition(positionInMs)
                setBufferedPosition(bufferedPositionInMs)
            }
        }
    )
}

@Composable
fun PlaybackButton(
    @DrawableRes resourceId: Int,
    description: String,
    onClick: () -> Unit = {},
) {
    Image(
        modifier = Modifier.size(32.dp).clickable(onClick = onClick),
        contentDescription = description,
        painter = painterResource(resourceId)
    )
}
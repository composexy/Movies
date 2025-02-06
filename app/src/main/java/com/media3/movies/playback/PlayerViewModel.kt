package com.media3.movies.playback

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.media3.movies.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : ViewModel() {
    private val _playerUiModel = MutableStateFlow(PlayerUiModel())
    val playerUiModel = _playerUiModel.asStateFlow()
    private val playerCoroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private var positionTrackingJob: Job? = null

    private val playerEventListener: Player.Listener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            if (videoSize !== VideoSize.UNKNOWN) {
                val videoWidth = videoSize.width
                val videoHeight = videoSize.height / videoSize.pixelWidthHeightRatio
                val videoAspectRatio = videoWidth / videoHeight
                _playerUiModel.value = _playerUiModel.value.copy(
                    videoAspectRatio = videoAspectRatio
                )
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                _playerUiModel.value = _playerUiModel.value.copy(
                    playbackState = PlaybackState.PLAYING
                )
            } else if (exoPlayer.playbackState == Player.STATE_READY) {
                _playerUiModel.value = _playerUiModel.value.copy(
                    playbackState = PlaybackState.PAUSED
                )
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            val state = when (playbackState) {
                Player.STATE_IDLE -> {
                    if (exoPlayer.playerError != null) {
                        PlaybackState.ERROR
                    } else {
                        PlaybackState.IDLE
                    }
                }
                Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                Player.STATE_READY -> {
                    if (exoPlayer.playWhenReady) {
                        PlaybackState.PLAYING
                    } else {
                        PlaybackState.PAUSED
                    }
                }
                Player.STATE_ENDED -> PlaybackState.COMPLETED
                else -> PlaybackState.IDLE
            }
            _playerUiModel.value = _playerUiModel.value.copy(
                playbackState = state
            )
            if (state == PlaybackState.ERROR) {
                showPlayerControls()
            }
            when (playbackState) {
                Player.STATE_READY -> {
                    startTrackingPlaybackPosition()
                }
                else -> {
                    stopTrackingPlaybackPosition()
                }
            }
            when (playbackState) {
                Player.STATE_READY -> {
                    hidePlaceHolderImage()
                }
                Player.STATE_ENDED,
                Player.STATE_IDLE -> {
                    showPlaceHolderImage()
                }
                else -> {}
            }
        }
    }

    private val exoPlayer = buildExoPlayer(application).apply {
        addListener(playerEventListener)
    }

    private fun buildExoPlayer(application: Application): ExoPlayer {
        return ExoPlayer.Builder(application).build()
    }

    private fun startTrackingPlaybackPosition() {
        positionTrackingJob = playerCoroutineScope.launch {
            while (true) {
                val newTimelineUiModel = buildTimelineUiModel()
                _playerUiModel.value = _playerUiModel.value.copy(
                    timelineUiModel = newTimelineUiModel
                )
                delay(1000)
            }
        }
    }

    private fun stopTrackingPlaybackPosition() {
        buildTimelineUiModel()
        positionTrackingJob?.cancel()
        positionTrackingJob = null
    }

    private fun buildTimelineUiModel(): TimelineUiModel? {
        val duration = exoPlayer.contentDuration
        if (duration == C.TIME_UNSET) return null
        val currentPosition = exoPlayer.contentPosition
        val bufferedPosition = exoPlayer.contentBufferedPosition
        return TimelineUiModel(
            durationInMs = duration,
            currentPositionInMs = currentPosition,
            bufferedPositionInMs = bufferedPosition
        )
    }

    fun showPlaceHolderImage() {
        _playerUiModel.value = _playerUiModel.value.copy(
            placeHolderImageResourceId = R.drawable.tears_of_steal_cover
        )
    }

    fun hidePlaceHolderImage() {
        _playerUiModel.value = _playerUiModel.value.copy(
            placeHolderImageResourceId = null
        )
    }

    fun showPlayerControls() {
        _playerUiModel.value = _playerUiModel.value.copy(
            playerControlsVisible = true
        )
    }

    fun hidePlayerControls() {
        _playerUiModel.value = _playerUiModel.value.copy(
            playerControlsVisible = false
        )
    }

    fun enterFullScreen() {
        _playerUiModel.value = _playerUiModel.value.copy(
            isFullScreen = true
        )
    }

    fun exitFullScreen() {
        _playerUiModel.value = _playerUiModel.value.copy(
            isFullScreen = false
        )
    }

    fun handleAction(action: Action) {
        when (action) {
            is AttachSurface -> {
                exoPlayer.setVideoSurface(action.surface)
            }
            DetachSurface -> {
                exoPlayer.setVideoSurface(null)
            }
            is FastForward -> {
                exoPlayer.seekTo(exoPlayer.currentPosition + action.amountInMs)
            }
            is Init -> {
                val mediaItem = MediaItem.Builder().setUri(action.streamUrl).build()
                exoPlayer.setMediaItem(mediaItem)
            }
            Pause -> {
                exoPlayer.pause()
            }
            Resume -> {
                exoPlayer.play()
            }
            is Rewind -> {
                exoPlayer.seekTo(exoPlayer.currentPosition - action.amountInMs)
            }
            is Seek -> {
                exoPlayer.seekTo(action.targetInMs)
            }
            is Start -> {
                with(exoPlayer) {
                    prepare()
                    play()
                    action.positionInMs?.let {
                        seekTo(it)
                    }
                }
            }
            Stop -> {
                exoPlayer.stop()
            }
        }
    }

    override fun onCleared() {
        exoPlayer.release()
    }

    companion object {
        fun buildFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlayerViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel type $modelClass")
                }
            }
        }
    }
}

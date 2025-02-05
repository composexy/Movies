package com.media3.movies.playback

data class PlayerUiModel(
    val videoAspectRatio: Float = 16.0f / 9.0f,
    val isFullScreen: Boolean = false,
    val playerControlsVisible: Boolean = false,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val timelineUiModel: TimelineUiModel? = null
)

enum class PlaybackState {
    IDLE, PLAYING, PAUSED, BUFFERING, COMPLETED, ERROR
}

data class TimelineUiModel(
    val durationInMs: Long,
    val currentPositionInMs: Long,
    val bufferedPositionInMs: Long
)

fun PlaybackState.isReady(): Boolean {
    return this == PlaybackState.PAUSED || this == PlaybackState.PLAYING
}

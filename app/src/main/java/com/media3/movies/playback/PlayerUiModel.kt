package com.media3.movies.playback

data class PlayerUiModel(
    val videoAspectRatio: Float = 16.0f / 9.0f,
    val isFullScreen: Boolean = false,
    val playerControlsVisible: Boolean = false
)
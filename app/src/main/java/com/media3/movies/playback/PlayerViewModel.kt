package com.media3.movies.playback

import android.app.Application
import android.net.Uri
import android.view.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(application: Application) : ViewModel() {
    private val _playerUiModel = MutableStateFlow(PlayerUiModel())
    val playerUiModel = _playerUiModel.asStateFlow()

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
    }

    private val exoPlayer = buildExoPlayer(application).apply {
        addListener(playerEventListener)
    }

    private fun buildExoPlayer(application: Application): ExoPlayer {
        return ExoPlayer.Builder(application).build()
    }

    fun startPlayback() {
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun stopPlayback() {
        exoPlayer.stop()
    }
    
    fun setStreamUrl(streamUrl: String) {
        val mediaItem = MediaItem.Builder().apply {
            setUri(Uri.parse(streamUrl))
        }
        exoPlayer.setMediaItem(mediaItem.build())
    }
    
    fun setSurface(surface: Surface) {
        exoPlayer.setVideoSurface(surface)
    }

    fun clearSurface() {
        exoPlayer.setVideoSurface(null)
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

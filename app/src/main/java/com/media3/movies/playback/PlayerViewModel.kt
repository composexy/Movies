package com.media3.movies.playback

import android.app.Application
import android.net.Uri
import android.view.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(application: Application) : ViewModel() {
    private val _playerUiModel = MutableStateFlow(PlayerUiModel())
    val playerUiModel = _playerUiModel.asStateFlow()

    private val exoPlayer = buildExoPlayer(application)

    private fun buildExoPlayer(application: Application): ExoPlayer {
        return ExoPlayer.Builder(application).build()
    }

    fun startPlayback() {
        exoPlayer.prepare()
        exoPlayer.play()
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

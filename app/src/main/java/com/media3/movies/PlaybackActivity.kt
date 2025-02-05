package com.media3.movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.media3.movies.playback.PlayerViewModel
import com.media3.movies.playback.VideoPlayer
import com.media3.movies.ui.theme.MoviesTheme

class PlaybackActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModel.buildFactory(application)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val streamUrl = intent.getStringExtra(KEY_STREAM_URL) ?: throw IllegalArgumentException()
        playerViewModel.setStreamUrl(streamUrl)

        setContent {
            val playerUiModel by playerViewModel.playerUiModel.collectAsState()

            MoviesTheme {
                Surface {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        VideoPlayer(playerViewModel = playerViewModel)
                    }
                }
            }

            LaunchedEffect(playerUiModel.isFullScreen) {
                val window = this@PlaybackActivity.window
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, window.decorView)
                if (playerUiModel.isFullScreen) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                } else {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playerViewModel.startPlayback()
    }

    override fun onStop() {
        super.onStop()
        playerViewModel.stopPlayback()
    }

    companion object {
        private const val KEY_STREAM_URL = "KEY_STREAM_URL"

        fun buildIntent(context: Context, streamUrl: String): Intent {
            val intent = Intent(context, PlaybackActivity::class.java)
            return intent.apply {
                putExtra(KEY_STREAM_URL, streamUrl)
            }
        }
    }
}

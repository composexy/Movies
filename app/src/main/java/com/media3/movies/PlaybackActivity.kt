package com.media3.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.media3.movies.playback.PlayerViewModel
import com.media3.movies.playback.VideoPlayer
import com.media3.movies.ui.theme.MoviesTheme

class PlaybackActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels {
        PlayerViewModel.buildFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val streamUrl = intent.getStringExtra(KEY_STREAM_URL) ?: throw IllegalArgumentException()
        playerViewModel.setStreamUrl(streamUrl)

        setContent {
            MoviesTheme {
                Surface {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        VideoPlayer(
                            modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
                            playerViewModel = playerViewModel
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        playerViewModel.startPlayback()
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

package com.media3.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.media3.movies.playback.VideoSurface
import com.media3.movies.ui.theme.MoviesTheme

class PlaybackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val streamUrl = intent.getStringExtra(KEY_STREAM_URL) ?: throw IllegalArgumentException()
        Log.e("PlaybackActivity", "StreamUrl $streamUrl")

        setContent {
            MoviesTheme {
                Surface {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        VideoSurface(
                            modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f)
                        )
                    }
                }
            }
        }
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

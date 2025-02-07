package com.media3.movies

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

val html = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Shaka Player Demo</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/shaka-player/4.3.2/shaka-player.compiled.js"></script>
    </head>
    <body>
        <video id="video" width="100%" controls autoplay></video>
        <script>
            async function initShakaPlayer() {
                const video = document.getElementById('video');
                const player = new shaka.Player(video);
                player.configure({
                    streaming: {
                        bufferingGoal: 60
                    }
                });
                const mpdUrl = '${StreamUrls.clearStreamUrl}';
                await player.load(mpdUrl);
            }

            document.addEventListener('DOMContentLoaded', initShakaPlayer);
        </script>
    </body>
    </html>
""".trimIndent()

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ShakaPlayer() {
    AndroidView(
        modifier = Modifier.fillMaxWidth().aspectRatio(16.0f / 9.0f),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                loadData(html, "text/html", "UTF-8")
            }
        },
        update = {}
    )
}
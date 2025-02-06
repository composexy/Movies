package com.media3.movies.playback

import android.view.Surface

sealed interface Action

data class Init(val streamUrl: String) : Action
data object Pause : Action
data object Resume : Action
data class Start(val positionInMs: Long? = null) : Action
data object Stop : Action
data class Rewind(val amountInMs: Int) : Action
data class FastForward(val amountInMs: Int) : Action
data class Seek(val targetInMs: Long) : Action
data class AttachSurface(val surface: Surface) : Action
data object DetachSurface : Action
data class SetVideoTrack(val track: VideoTrack) : Action
data class SetAudioTrack(val track: AudioTrack) : Action
data class SetSubtitleTrack(val track: SubtitleTrack) : Action

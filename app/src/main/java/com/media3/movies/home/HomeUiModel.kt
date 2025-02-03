package com.media3.movies.home

import com.media3.movies.R

data class HomeUiModel(
    val coverImageResourceId: Int = R.drawable.tears_of_steal_cover,
    val streamUrl: String = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd",
    val attribution: String = "(CC) Blender Foundation | mango.blender.org",
    val description: String = "The film’s premise is about a group of warriors and scientists, " +
            "who gathered at the \"Oude Kerk\" in Amsterdam to stage a crucial event from the " +
            "past, in a desperate attempt to rescue the world from destructive robots.",
)

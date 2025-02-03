package com.media3.movies.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _homeUiModel = MutableStateFlow(HomeUiModel())
    val homeUiModel = _homeUiModel.asStateFlow()
}

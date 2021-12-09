package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.ScreenOutput
import com.example.flow_arch.common.network.Movie

sealed class MainOutput: ScreenOutput {
    data class Display(
        val counter: Int = 0,
        val moviesState: MovieState = MovieState.Loading
    ) : MainOutput()

    object OpenNextScreen : MainOutput()
}

sealed class MovieState {
    object Retry: MovieState()
    object Loading: MovieState()
    data class Display(val list: List<Movies>) : MovieState()
}

sealed class Movies {
    data class Display(val movie: Movie) : Movies()
    data class Loading(val page: Int) : Movies()
}

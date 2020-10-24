package com.example.flow_arch.main.usecases

import com.example.flow_arch.common.network.Movie

sealed class Result {
    sealed class UiUpdate : Result() {
        sealed class MovieList : UiUpdate() {
            object Loading: MovieList()
            data class Display(val movies: List<Movie>) : MovieList()
            object Error: MovieList()
        }
        object IncrementNumber : UiUpdate()
    }

    sealed class Navigation : Result() {
        object MoveToNextPage : Navigation()
    }
}

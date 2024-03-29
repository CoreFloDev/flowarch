package com.example.flow_arch.common.repo

import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.network.MovieApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(
    private val movieApi: MovieApi
) {

    fun getMovieList(page: Int): Flow<List<Movie>> = flow {
        emit(movieApi.getMovieList(page).result.map {
            it.copy(image = "https://image.tmdb.org/t/p/w200/" + it.image)
        })
    }
}

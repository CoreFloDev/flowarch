package com.example.flow_arch.common.repo

import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.network.MovieApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(
    private val movieApi: MovieApi
) {

    fun getMovieList(): Flow<List<Movie>> = flow {
        emit(movieApi.getMovieList().result)
    }
}

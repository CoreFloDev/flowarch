package com.example.flow_arch.common.repo

import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.network.MovieApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(
    private val movieApi: MovieApi
) {

    @FlowPreview
    fun getMovieList(): Flow<List<Movie>> = flow {
        val query = movieApi.getMovieList()
        println("coucou query $query")
        emit(query.result)
    }
}

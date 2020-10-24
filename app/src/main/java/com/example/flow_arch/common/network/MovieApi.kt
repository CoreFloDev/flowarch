package com.example.flow_arch.common.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET

interface MovieApi {

    @GET("3/movie/top_rated?api_key=06c477fb6235927e8e8ea7e96b18133c")
    suspend fun getMovieList(): ResultList
}

@JsonClass(generateAdapter = true)
data class ResultList(
    @Json(name = "results") val result: List<Movie> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val image: String,
    @Json(name = "title") val title: String
)

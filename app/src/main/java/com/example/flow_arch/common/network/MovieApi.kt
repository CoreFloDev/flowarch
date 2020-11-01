package com.example.flow_arch.common.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface MovieApi {

    @GET("3/movie/top_rated?api_key=06c477fb6235927e8e8ea7e96b18133c")
    suspend fun getMovieList(): ResultList
}

//class MovieApiImplementation(
//    private val client: HttpClient
//): MovieApi {
//    override suspend fun getMovieList(): ResultList {
//        return client.get("https://api.themoviedb.org/3/movie/top_rated?api_key=06c477fb6235927e8e8ea7e96b18133c")
//    }
//}

@Serializable
data class ResultList(
    @SerialName("results") val result: List<Movie> = emptyList()
)

@Serializable
data class Movie(
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val image: String,
    @SerialName( "title") val title: String
)

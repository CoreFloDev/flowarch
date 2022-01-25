package com.example.flow_arch.common.di

import android.content.Context
import com.example.flow_arch.common.network.MovieApi
import com.example.flow_arch.common.network.MovieApiImplementation
import com.example.flow_arch.common.repo.MovieRepository
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
class AppModule(private val context: Context) {

    @Provides
    @AppScope
    fun provideContext() = context

    @Provides
    @AppScope
    fun provideHttpClient() = HttpClient {
        install(DefaultRequest) {
            accept(ContentType.Application.Json)
        }
        // Json
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    @Provides
    @AppScope
    fun provideMovieApi(httpClient: HttpClient): MovieApi = MovieApiImplementation(httpClient)

    @Provides
    @AppScope
    fun provideMovieRepository(movieApi: MovieApi) = MovieRepository(movieApi)
}

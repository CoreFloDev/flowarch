package com.example.flow_arch.common.di

import android.content.Context
import com.example.flow_arch.common.network.MovieApi
import com.example.flow_arch.common.network.MovieApiImplementation
import com.example.flow_arch.common.repo.MovieRepository
import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

@Module
class AppModule(private val context: Context) {

    @Provides
    @AppScope
    fun provideContext() = context

    @Provides
    @AppScope
    fun provideHttpClient() = HttpClient {
        // Json
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
            accept(ContentType.Application.Json)
            charset("utf-8")
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

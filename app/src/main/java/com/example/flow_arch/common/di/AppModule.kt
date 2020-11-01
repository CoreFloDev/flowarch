package com.example.flow_arch.common.di

import android.content.Context
import com.example.flow_arch.common.network.MovieApi
import com.example.flow_arch.common.repo.MovieRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
class AppModule(private val context: Context) {

    @Provides
    @AppScope
    fun provideContext() = context

    @Provides
    @AppScope
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(json.asConverterFactory("application/json;charset=utf-8".toMediaType()))
            .build()
//    @Provides
//    @AppScope
//    fun provideHttpClient() = HttpClient {
//        HttpClient(OkHttp) {
//            // Json
//            install(JsonFeature) {
//                serializer = KotlinxSerializer(json)
//                accept(ContentType.Application.Json)
//                charset("utf-8")
//            }
//
//        }
//    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

//    @Provides
//    @AppScope
//    fun provideMovieApi(httpClient: HttpClient): MovieApi = MovieApiImplementation(httpClient)

        @Provides
    @AppScope
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @AppScope
    fun provideMovieRepository(movieApi: MovieApi) = MovieRepository(movieApi)
}

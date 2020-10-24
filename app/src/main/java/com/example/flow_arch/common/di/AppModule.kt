package com.example.flow_arch.common.di

import android.content.Context
import com.example.flow_arch.common.network.MovieApi
import com.example.flow_arch.common.repo.MovieRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @AppScope
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @AppScope
    fun provideMovieRepository(movieApi: MovieApi) = MovieRepository(movieApi)
}

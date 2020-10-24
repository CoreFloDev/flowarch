package com.example.flow_arch.main.di

import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.common.repo.MovieRepository
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.main.arch.MainScreen
import com.example.flow_arch.main.usecases.IncrementCounterUseCase
import com.example.flow_arch.main.usecases.LoadMovieListUseCase
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideScreen(
        incrementCounterUseCase: IncrementCounterUseCase,
        loadMovieListUseCase: LoadMovieListUseCase
    ) : Screen<MainInput, MainOutput> = MainScreen(
        incrementCounterUseCase,
        loadMovieListUseCase
    )

    @Provides
    @MainScope
    fun provideInitialUseCase(movieRepository: MovieRepository) = LoadMovieListUseCase(movieRepository)

    @Provides
    @MainScope
    fun provideIncrementNumberUseCase() = IncrementCounterUseCase()
}

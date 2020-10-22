package com.example.flow_arch.main.di

import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.main.arch.MainScreen
import com.example.flow_arch.main.usecases.IncrementNumberUseCase
import com.example.flow_arch.main.usecases.InitialUseCase
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideScreen(
        incrementNumberUseCase: IncrementNumberUseCase,
        initialUseCase: InitialUseCase
    ) : Screen<MainInput, MainOutput> = MainScreen(
        incrementNumberUseCase,
        initialUseCase
    )

    @Provides
    @MainScope
    fun provideInitialUseCase() = InitialUseCase()

    @Provides
    @MainScope
    fun provideIncrementNumberUseCase() = IncrementNumberUseCase()
}

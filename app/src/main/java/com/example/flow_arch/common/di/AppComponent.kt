package com.example.flow_arch.common.di

import com.example.flow_arch.common.repo.MovieRepository
import dagger.Component

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun movieRepo(): MovieRepository
}

package com.example.flow_arch.main.di

import com.example.flow_arch.common.di.AppComponent
import dagger.Component

@MainScope
@Component(modules = [MainModule::class], dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(mainStateHolder: MainStateHolder)
}

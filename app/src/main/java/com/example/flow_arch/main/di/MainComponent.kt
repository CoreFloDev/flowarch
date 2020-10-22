package com.example.flow_arch.main.di

import dagger.Component

@MainScope
@Component(modules = [MainModule::class])
interface MainComponent {
    fun inject(mainStateHolder: MainStateHolder)
}

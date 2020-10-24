package com.example.flow_arch.main.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flow_arch.common.FlowApp
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import javax.inject.Inject

class MainStateHolder(app: Application): AndroidViewModel(app) {

    @Inject
    lateinit var screen: Screen<MainInput, MainOutput>

    init {
        DaggerMainComponent.builder()
            .appComponent(FlowApp.appComponent(app))
            .build()
            .inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        screen.terminate()
    }
}

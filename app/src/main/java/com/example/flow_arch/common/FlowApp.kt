package com.example.flow_arch.common

import android.app.Application
import android.content.Context
import com.example.flow_arch.common.di.AppComponent
import com.example.flow_arch.common.di.AppModule
import com.example.flow_arch.common.di.DaggerAppComponent

class FlowApp : Application() {

    private val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(baseContext))
            .build()
    }

    companion object {

        fun appComponent(context: Context) = (context.applicationContext as FlowApp).applicationComponent
    }
}

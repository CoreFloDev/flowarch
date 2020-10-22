package com.example.flow_arch.main.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.main.arch.MainScreen
import com.example.flow_arch.R
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.common.arch.ScreenView
import com.example.flow_arch.main.di.MainStateHolder
import com.example.flow_arch.main.usecases.IncrementNumberUseCase
import com.example.flow_arch.main.usecases.InitialUseCase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity(), ScreenView<MainInput, MainOutput> {

    private lateinit var screen: Screen<MainInput, MainOutput>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screen = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(MainStateHolder::class.java)
            .screen

        screen.attach(this)
    }

    override fun onDestroy() {
        screen.detach()
        super.onDestroy()
    }

    fun View.clicks(): Flow<Unit> = callbackFlow {
        setOnClickListener { offer(Unit) }
        awaitClose { setOnClickListener(null) }
    }

    override fun render(output: MainOutput) =
        when (output) {
            is MainOutput.Display -> {
                findViewById<TextView>(R.id.action_tv).text = output.counter.toString()
            }
            MainOutput.OpenNextScreen -> Unit //TODO
        }

    override fun inputs(): Flow<MainInput> =
        findViewById<Button>(R.id.action_btn).clicks().map { MainInput.Click }
}

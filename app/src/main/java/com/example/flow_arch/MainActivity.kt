package com.example.flow_arch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.flow_arch.arch.Screen
import com.example.flow_arch.arch.ScreenView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity(), ScreenView<MainInput, MainOutput> {

    private val screen: Screen<MainInput, MainOutput> = MainScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun render(output: MainOutput) {
        when (output) {
            is MainOutput.Display -> {
                findViewById<TextView>(R.id.action_tv).text = output.counter.toString()
            }
        }
    }

    override fun inputs(): Flow<MainInput> =
        findViewById<Button>(R.id.action_btn).clicks().map { MainInput.Click }
}

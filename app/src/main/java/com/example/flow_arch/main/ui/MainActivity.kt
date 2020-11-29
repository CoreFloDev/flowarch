package com.example.flow_arch.main.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.R
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.common.arch.ScreenView
import com.example.flow_arch.main.arch.MovieState
import com.example.flow_arch.main.di.MainStateHolder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class MainActivity : AppCompatActivity(), ScreenView<MainInput, MainOutput> {

    private lateinit var screen: Screen<MainInput, MainOutput>
    private val adapter = MovieAdapter().apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screen = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(MainStateHolder::class.java)
            .screen

        val rv = findViewById<RecyclerView>(R.id.movies_rv)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

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

                findViewById<Button>(R.id.movies_pb).isVisible = output.moviesState is MovieState.Loading
                findViewById<TextView>(R.id.movies_retry_btn).isVisible = output.moviesState is MovieState.Retry
                findViewById<RecyclerView>(R.id.movies_rv).isVisible = output.moviesState is MovieState.Display

                if (output.moviesState is MovieState.Display) {
                    adapter.update(output.moviesState.list)
                } else Unit
            }
            MainOutput.OpenNextScreen -> Unit //TODO
        }

    override fun inputs(): Flow<MainInput> = listOf(
        findViewById<Button>(R.id.action_btn).clicks().map { MainInput.Click },
        findViewById<Button>(R.id.movies_retry_btn).clicks().map { MainInput.RetryClicked }
    ).merge()
}

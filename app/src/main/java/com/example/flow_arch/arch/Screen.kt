package com.example.flow_arch.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class Screen<I : ScreenInput, O : ScreenOutput> {

    private val scope: CoroutineScope = MainScope()

    protected val input: Channel<I> = Channel()
    private val output by lazy { output() }

    protected abstract fun output(): Flow<O>

    abstract fun terminate()

    fun attach(view: ScreenView<I, O>) {
        output
            .flowOn(Dispatchers.Default)
            .onEach(view::render)
            .launchIn(scope)

        view.inputs()
            .flowOn(Dispatchers.Default)
            .onEach(input::send)
            .launchIn(scope)
    }

    fun detach() {
        scope.cancel()
    }
}

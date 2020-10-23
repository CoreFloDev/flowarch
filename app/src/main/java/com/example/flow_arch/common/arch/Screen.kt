package com.example.flow_arch.common.arch

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class Screen<I : ScreenInput, O : ScreenOutput> {

    private val viewScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    protected val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    protected val input: Channel<I> = Channel()
    private val output by lazy { output() }

    protected abstract fun output(): Flow<O>

    abstract fun terminate()

    fun attach(view: ScreenView<I, O>) {
        println("coucou3 attach")

        output
            .onEach(view::render)
            .launchIn(viewScope)

        view.inputs()
            .flowOn(Dispatchers.Default)
            .onEach(input::send)
            .launchIn(scope)
    }

    fun detach() {
        println("coucou3 detach")
        viewScope.cancel()
    }
}

package com.example.flow_arch

import com.example.flow_arch.arch.Screen
import com.example.flow_arch.usecases.Action
import kotlinx.coroutines.flow.*

class MainScreen : Screen<MainInput, MainOutput>() {

    override fun output(): Flow<MainOutput> = input.consumeAsFlow()
        .compose(inputToAction())
        .scan(MainOutput.Display()) { previous, _ ->
            previous.copy(counter = previous.counter + 1)
        }

    override fun terminate() {
        // TODO still needed?
    }

    companion object {
        fun inputToAction(): FlowTransformer<MainInput, Action> = { flow ->
            flow.map {
                when (it) {
                    MainInput.Click -> Action.IncrementNumber
                } as Action
            }.onStart { emit(Action.InitialAction) }
        }
    }
}

typealias FlowTransformer<A, B> = (Flow<A>) -> Flow<B>

private fun <A, B> Flow<A>.compose(lambda: FlowTransformer<A, B>): Flow<B> = lambda(this)

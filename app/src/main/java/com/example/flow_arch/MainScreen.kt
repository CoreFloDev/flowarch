package com.example.flow_arch

import com.example.flow_arch.arch.Screen
import kotlinx.coroutines.flow.*

class MainScreen : Screen<MainInput, MainOutput>() {

    override fun output(): Flow<MainOutput> = input.consumeAsFlow()
        .scan(MainOutput.Display()) { previous, _ ->
            previous.copy(counter = previous.counter + 1)
        }

    override fun terminate() {
        // TODO still needed?
    }
}


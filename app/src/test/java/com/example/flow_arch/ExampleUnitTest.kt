package com.example.flow_arch

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `kotlin language error maybe`() {
        val channel = Channel<String>()

        channel.receiveAsFlow()
            .map<String, State> { State.Content(it) }
            .catch { emit(State.Error) }
            .onStart { emit(State.Loading) }
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        data class Content(val content: String) : State()
    }
}

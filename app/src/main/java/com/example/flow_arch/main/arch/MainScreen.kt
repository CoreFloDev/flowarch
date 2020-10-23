package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.main.usecases.Action
import com.example.flow_arch.main.usecases.IncrementNumberUseCase
import com.example.flow_arch.main.usecases.InitialUseCase
import com.example.flow_arch.main.usecases.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

class MainScreen(
    private val incrementNumberUseCase: IncrementNumberUseCase,
    private val initialUseCase: InitialUseCase
) : Screen<MainInput, MainOutput>() {

    override fun output(): Flow<MainOutput> = input.receiveAsFlow()
        .compose(inputToAction())
        .compose { stream ->
            val upstream = stream.shareIn(scope, SharingStarted.Eagerly)
            listOf(
                upstream.filterIsInstance<Action.InitialAction>().compose(initialUseCase()),
                upstream.filterIsInstance<Action.IncrementNumber>().compose(incrementNumberUseCase())
            )
                .merge()
        }
        .compose(convertResultToOutput(scope))

    override fun terminate() {
        scope.cancel()
    }

    companion object {
        fun inputToAction() = FlowTransformer<MainInput, Action> { flow ->
            flow.map { input ->
                when (input) {
                    MainInput.Click -> Action.IncrementNumber as Action
                }
            }.onStart { emit(Action.InitialAction) }
        }

        fun convertResultToOutput(clear: CoroutineScope) = FlowTransformer<Result, MainOutput> { stream ->
                val upsteam = stream.shareIn(clear, SharingStarted.Eagerly)

                listOf(
                    upsteam.filterIsInstance<Result.UiUpdate>()
                        .compose(reducingUiState())
                        .onEach { println("coucou2 $it") }
                        .stateIn(clear, SharingStarted.Eagerly, MainOutput.Display())
                        .onEach { println("coucou passed $it") },
                    upsteam.filterIsInstance<Result.Navigation>()
                        .compose(reducingNavigation())
                ).merge()
            }

        private fun reducingUiState() = FlowTransformer<Result.UiUpdate, MainOutput.Display> { stream ->
                stream.scan(MainOutput.Display()) { previous, new ->
                    when (new) {
                        Result.UiUpdate.IncrementNumber -> previous.copy(counter = previous.counter + 1)
                    }
                }
            }

        private fun reducingNavigation() = FlowTransformer<Result.Navigation, MainOutput> { stream ->
            stream.map {output ->
                when (output) {
                    Result.Navigation.MoveToNextPage -> MainOutput.OpenNextScreen
                }
            }
        }
    }
}

fun interface FlowTransformer<A, B> : (Flow<A>) -> Flow<B>

private fun <A, B> Flow<A>.compose(lambda: FlowTransformer<A, B>): Flow<B> = lambda(this)

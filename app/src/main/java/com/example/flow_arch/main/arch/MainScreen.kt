package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.FlowTransformer
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.main.usecases.Action
import com.example.flow_arch.main.usecases.IncrementCounterUseCase
import com.example.flow_arch.main.usecases.LoadMovieListUseCase
import com.example.flow_arch.main.usecases.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn

class MainScreen(
    private val incrementCounterUseCase: IncrementCounterUseCase,
    private val loadMovieListUseCase: LoadMovieListUseCase
) : Screen<MainInput, MainOutput>() {

    override fun output(): Flow<MainOutput> = input.receiveAsFlow()
        .let(inputToAction())
        .let { stream ->
            val upstream = stream.shareIn(scope, SharingStarted.Eagerly, 1)

            listOf(
                upstream.filterIsInstance<Action.InitialAction>().let(loadMovieListUseCase()),
                upstream.filterIsInstance<Action.IncrementNumber>().let(incrementCounterUseCase())
            )
                .merge()
        }
        .let(convertResultToOutput(scope))

    companion object {
        fun inputToAction() = FlowTransformer<MainInput, Action> { flow ->
            flow.map { input ->
                when (input) {
                    MainInput.Click -> Action.IncrementNumber
                    MainInput.RetryClicked -> Action.InitialAction
                }
            }
                .onStart { emit(Action.InitialAction) }
        }

        fun convertResultToOutput(clear: CoroutineScope) = FlowTransformer<Result, MainOutput> { stream ->
                val upstream = stream.shareIn(clear, SharingStarted.Lazily)

                listOf(
                    upstream.filterIsInstance<Result.UiUpdate>()
                        .let(reducingUiState())
                        .shareIn(clear, SharingStarted.Lazily, 1),
                    upstream.filterIsInstance<Result.Navigation>()
                        .let(reducingNavigation())
                ).merge()
            }

        private fun reducingUiState() = FlowTransformer<Result.UiUpdate, MainOutput.Display> { stream ->
                stream.scan(MainOutput.Display()) { previous, new ->
                    when (new) {
                        is Result.UiUpdate.MovieList -> previous.copy(moviesState = when (new) {
                            Result.UiUpdate.MovieList.Loading -> MovieState.Loading
                            is Result.UiUpdate.MovieList.Display -> MovieState.Display(new.movies)
                            Result.UiUpdate.MovieList.Error -> MovieState.Retry
                        })
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

package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.main.usecases.Action
import com.example.flow_arch.main.usecases.IncrementCounterUseCase
import com.example.flow_arch.main.usecases.LoadMovieListUseCase
import com.example.flow_arch.main.usecases.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class MainScreen(
    private val incrementCounterUseCase: IncrementCounterUseCase,
    private val loadMovieListUseCase: LoadMovieListUseCase
) : Screen<MainInput, MainOutput>() {

    override fun output(): Flow<MainOutput> = input.receiveAsFlow()
        .compose(inputToAction())
        .compose { stream ->
            val upstream = stream.shareIn(scope, SharingStarted.Lazily)

            listOf(
                upstream.filterIsInstance<Action.InitialAction>().compose(loadMovieListUseCase()),
                upstream.filterIsInstance<Action.IncrementNumber>().compose(incrementCounterUseCase())
            )
                .merge()
        }
        .compose(convertResultToOutput(scope))

    companion object {
        fun inputToAction() = FlowTransformer<MainInput, Action> { flow ->
            flow.map { input ->
                when (input) {
                    MainInput.Click -> Action.IncrementNumber as Action
                }
            }.onStart {
                emit(Action.InitialAction)
            }
        }

        fun convertResultToOutput(clear: CoroutineScope) = FlowTransformer<Result, MainOutput> { stream ->
                val upsteam = stream.shareIn(clear, SharingStarted.Eagerly)

                listOf(
                    upsteam.filterIsInstance<Result.UiUpdate>()
                        .compose(reducingUiState())
                        .shareIn(clear, SharingStarted.Eagerly, 1),
                    upsteam.filterIsInstance<Result.Navigation>()
                        .compose(reducingNavigation())
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

fun interface FlowTransformer<A, B> : (Flow<A>) -> Flow<B>

private fun <A, B> Flow<A>.compose(lambda: FlowTransformer<A, B>): Flow<B> = this.let(lambda)
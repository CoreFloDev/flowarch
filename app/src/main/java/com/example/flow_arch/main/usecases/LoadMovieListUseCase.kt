package com.example.flow_arch.main.usecases

import com.example.flow_arch.common.repo.MovieRepository
import com.example.flow_arch.main.arch.FlowTransformer
import kotlinx.coroutines.flow.*

class LoadMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke() = FlowTransformer<Action.InitialAction, Result> { flow ->
        flow.flatMapLatest {
            movieRepository.getMovieList()
                .map {
                    Result.UiUpdate.MovieList.Display(it) as Result
                }
                .catch { Result.UiUpdate.MovieList.Error }
                .onStart { emit(Result.UiUpdate.MovieList.Loading) }
        }.onEach {
            println("coucou $it")
        }
    }
}

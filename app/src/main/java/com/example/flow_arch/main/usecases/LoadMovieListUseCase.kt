package com.example.flow_arch.main.usecases

import com.example.flow_arch.common.repo.MovieRepository
import com.example.flow_arch.main.arch.FlowTransformer
import kotlinx.coroutines.flow.*

class LoadMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke() : (Flow<Action.InitialAction>) -> Flow<Result> = { flow ->
        flow.flatMapLatest {
            movieRepository.getMovieList()
                .map { Result.UiUpdate.MovieList.Display(it) as Result }
                .catch { emit(Result.UiUpdate.MovieList.Error) }
                .onStart { emit(Result.UiUpdate.MovieList.Loading) }
        }
    }
}

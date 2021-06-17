package com.example.flow_arch.main.usecases

import com.example.flow_arch.common.arch.FlowTransformer
import com.example.flow_arch.common.repo.MovieRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class LoadMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke() = FlowTransformer<Action.InitialAction, Result> { flow ->
        flow.flatMapLatest {
            movieRepository.getMovieList()
                .map { Result.UiUpdate.MovieList.Display(it) as Result }
                .catch { emit(Result.UiUpdate.MovieList.Error) }
                .onStart { emit(Result.UiUpdate.MovieList.Loading) }
        }
    }
}

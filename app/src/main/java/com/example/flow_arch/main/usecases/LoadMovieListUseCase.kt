package com.example.flow_arch.main.usecases

import com.example.flow_arch.common.arch.FlowTransformer
import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.repo.MovieRepository
import com.example.flow_arch.main.arch.Movies
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class LoadMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke() = FlowTransformer<Action.LoadPage, Result> { flow ->
        flow.flatMapLatest { action ->
            movieRepository.getMovieList(action.page)
                .map<List<Movie>, Result> { movies -> Result.UiUpdate.MovieList.Display(movies.map { Movies.Display(it) } + Movies.Loading(action.page + 1)) }
                .catch { emit(Result.UiUpdate.MovieList.Error) }
                .onStart { emit(Result.UiUpdate.MovieList.Loading) }
        }
    }
}

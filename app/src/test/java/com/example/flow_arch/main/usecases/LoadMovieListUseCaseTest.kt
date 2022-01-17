package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.repo.MovieRepository
import com.example.flow_arch.main.arch.Movies
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadMovieListUseCaseTest {

    private val movieRepo: MovieRepository = mockk()

    private val useCase = LoadMovieListUseCase(movieRepo)

    @Test
    fun `test nominal case`() = TestScope().runTest {
        every { movieRepo.getMovieList(1) } returns flowOf(listOf(Movie(OVERVIEW, IMAGE, TITLE)))

        flowOf(Action.LoadPage(1))
            .let(useCase())
            .test {
                assertEquals(Result.UiUpdate.MovieList.Loading, awaitItem())
                assertEquals(
                    Result.UiUpdate.MovieList.Display(
                        listOf(
                            Movies.Display(Movie(
                                OVERVIEW,
                                IMAGE,
                                TITLE
                            ))
                        )
                    ), awaitItem()
                )
                awaitComplete()
            }
    }

    @Test
    fun `test error case`() = TestScope().runTest {
        every { movieRepo.getMovieList(1) } returns flow { throw Exception() }

        flowOf(Action.LoadPage(1))
            .let(useCase())
            .test {
                assertEquals(Result.UiUpdate.MovieList.Loading, awaitItem())
                assertEquals(Result.UiUpdate.MovieList.Error, awaitItem())
                awaitComplete()
            }
    }

    companion object {
        private const val OVERVIEW = "overview"
        private const val IMAGE = "image"
        private const val TITLE = "title"
    }
}

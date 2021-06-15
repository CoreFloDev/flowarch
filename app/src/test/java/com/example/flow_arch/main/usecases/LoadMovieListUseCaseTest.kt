package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.repo.MovieRepository
import io.mockk.every
import io.mockk.mockk
import java.lang.Exception
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadMovieListUseCaseTest {

    private val movieRepo: MovieRepository = mockk()

    private val useCase = LoadMovieListUseCase(movieRepo)

    @ExperimentalTime
    @Test
    fun `test nominal case`() {
        every { movieRepo.getMovieList() } returns flowOf(listOf(Movie(OVERVIEW, IMAGE, TITLE)))

        runBlockingTest {
            flowOf(Action.InitialAction)
                .let(useCase())
                .test {
                    assertEquals(Result.UiUpdate.MovieList.Loading, expectItem())
                    assertEquals(Result.UiUpdate.MovieList.Display(listOf(Movie(OVERVIEW, IMAGE, TITLE))), expectItem())
                    expectComplete()
                }
        }
    }

    @ExperimentalTime
    @Test
    fun `test error case`() {
        every { movieRepo.getMovieList() } returns flow { throw Exception() }

        runBlockingTest {
            flowOf(Action.InitialAction)
                .let(useCase())
                .test {
                    assertEquals(Result.UiUpdate.MovieList.Loading, expectItem())
                    assertEquals(Result.UiUpdate.MovieList.Error, expectItem())
                    expectComplete()
                }
        }
    }

    companion object {
        private const val OVERVIEW = "overview"
        private const val IMAGE = "image"
        private const val TITLE = "title"
    }
}

package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import com.example.flow_arch.common.repo.MovieRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.ExperimentalTime

class LoadMovieListUseCaseTest {

    private val movieRepo: MovieRepository = mockk()

    private val useCase = LoadMovieListUseCase(movieRepo)

    @ExperimentalTime
    @Test
    fun `test nominal case`() {
        every { movieRepo.getMovieList() } returns emptyFlow()

        runBlockingTest {
            flowOf(Action.InitialAction)
                .let(useCase())
                .test {
                    assertEquals(expectItem(), Result.UiUpdate.IncrementNumber)
                    expectComplete()
                }
        }
    }


    @Test
    fun `test shared flow error`() {
        runBlockingTest {
            var numberOfReceived = 0
            var shareScope = CoroutineScope(Dispatchers.Default)

            for (i in 1..100) {
                var mainScope = CoroutineScope(Dispatchers.Default)

                println("flow started $i")


                val upstream = emptyFlow<Content>()
                    .onStart { emit(Content.A) }
                    .shareIn(shareScope, SharingStarted.Lazily)

                listOf(
                    upstream.filterIsInstance<Content.A>().onEach {
                        println("content received $it")
                        numberOfReceived++
                        mainScope.cancel()
                    },
                    upstream.filterIsInstance<Content.B>()
                ).merge()
                    .launchIn(mainScope)

            }
            println(numberOfReceived)
        }

    }

    sealed class Content {
        object A : Content()
        object B : Content()
    }
}

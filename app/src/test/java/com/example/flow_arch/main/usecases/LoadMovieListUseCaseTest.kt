package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

class LoadMovieListUseCaseTest {

    private val useCase = LoadMovieListUseCase()

    @ExperimentalTime
    @Test
    fun `test nominal case`() {
        runBlocking {
            flowOf(Action.InitialAction)
                .let(useCase())
                .test {
                    assertEquals(expectItem(), Result.UiUpdate.IncrementNumber)
                    expectComplete()
                }
        }
    }
}

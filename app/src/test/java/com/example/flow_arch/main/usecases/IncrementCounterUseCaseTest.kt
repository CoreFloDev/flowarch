package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IncrementCounterUseCaseTest {

    private val useCase = IncrementCounterUseCase()

    @ExperimentalTime
    @Test
    fun `nominal test case`() {
        runBlockingTest {
            flowOf(Action.IncrementNumber)
                .let(useCase())
                .test {
                    assertEquals(Result.UiUpdate.IncrementNumber, awaitItem())
                    awaitComplete()
                }
        }
    }
}

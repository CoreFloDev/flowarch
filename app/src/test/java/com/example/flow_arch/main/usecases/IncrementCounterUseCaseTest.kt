package com.example.flow_arch.main.usecases

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class IncrementCounterUseCaseTest {

    private val useCase = IncrementCounterUseCase()

    @Test
    fun `nominal test case`() = TestScope().runTest {
        flowOf(Action.IncrementNumber)
            .let(useCase())
            .test {
                assertEquals(Result.UiUpdate.IncrementNumber, awaitItem())
                awaitComplete()
            }
    }
}

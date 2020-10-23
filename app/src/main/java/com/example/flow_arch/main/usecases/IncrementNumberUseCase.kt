package com.example.flow_arch.main.usecases

import com.example.flow_arch.main.arch.FlowTransformer
import kotlinx.coroutines.flow.map

class IncrementNumberUseCase {

    operator fun invoke() = FlowTransformer<Action.IncrementNumber, Result> { flow ->
        flow.map {
            Result.UiUpdate.IncrementNumber
        }
    }
}

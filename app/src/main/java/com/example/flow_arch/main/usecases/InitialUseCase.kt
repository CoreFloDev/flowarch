package com.example.flow_arch.main.usecases

import com.example.flow_arch.main.arch.FlowTransformer
import kotlinx.coroutines.flow.map

class InitialUseCase {

    operator fun invoke() = FlowTransformer<Action.InitialAction, Result> { flow ->
        flow.map {
            Result.UiUpdate.IncrementNumber
        }
    }
}

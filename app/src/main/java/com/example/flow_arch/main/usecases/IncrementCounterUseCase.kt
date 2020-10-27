package com.example.flow_arch.main.usecases

import com.example.flow_arch.main.arch.FlowTransformer
import kotlinx.coroutines.flow.map

class IncrementCounterUseCase : UseCase<Action.IncrementNumber> {

    override operator fun invoke() = FlowTransformer<Action.IncrementNumber, Result> { flow ->
        flow.map {
            Result.UiUpdate.IncrementNumber
        }
    }
}

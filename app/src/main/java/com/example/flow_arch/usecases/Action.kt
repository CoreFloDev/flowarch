package com.example.flow_arch.usecases

sealed class Action {
    object InitialAction : Action()
    object IncrementNumber : Action()
}

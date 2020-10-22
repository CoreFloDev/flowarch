package com.example.flow_arch.main.usecases

sealed class Action {
    object InitialAction : Action()
    object IncrementNumber : Action()
}

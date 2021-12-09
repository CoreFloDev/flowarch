package com.example.flow_arch.main.usecases

sealed class Action {
    data class LoadPage(val page: Int) : Action()
    object IncrementNumber : Action()
}

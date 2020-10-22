package com.example.flow_arch.main.usecases

sealed class Result {
    sealed class UiUpdate : Result() {
        object IncrementNumber : UiUpdate()
    }

    sealed class Navigation : Result() {
        object MoveToNextPage : Navigation()
    }
}

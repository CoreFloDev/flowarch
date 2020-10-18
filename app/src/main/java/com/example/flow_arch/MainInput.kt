package com.example.flow_arch

import com.example.flow_arch.arch.ScreenInput

sealed class MainInput: ScreenInput {
    object Click : MainInput()
}

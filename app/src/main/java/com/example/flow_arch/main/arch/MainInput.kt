package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.ScreenInput

sealed class MainInput: ScreenInput {
    object Click : MainInput()
}

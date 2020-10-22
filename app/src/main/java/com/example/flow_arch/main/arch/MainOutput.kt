package com.example.flow_arch.main.arch

import com.example.flow_arch.common.arch.ScreenOutput

sealed class MainOutput: ScreenOutput {
    data class Display(val counter: Int = 0) : MainOutput()

    object OpenNextScreen : MainOutput()
}

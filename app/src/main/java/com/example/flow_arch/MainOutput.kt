package com.example.flow_arch

import com.example.flow_arch.arch.ScreenOutput

sealed class MainOutput: ScreenOutput {
    data class Display(val counter: Int = 0) : MainOutput()
}

package com.example.flow_arch.common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import coil.load as coilLoad

class NetworkImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attr, defStyleAttr) {

    fun load(url: String) {
        coilLoad(url)
    }
}

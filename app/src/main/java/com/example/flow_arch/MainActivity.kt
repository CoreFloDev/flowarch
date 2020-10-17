package com.example.flow_arch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            findViewById<Button>(R.id.action_btn).clicks()
                .flowOn(Dispatchers.Default)
                .scan(0) { prev, _ ->
                    prev + 1
                }
                .collect {
                    findViewById<TextView>(R.id.action_tv).text = it.toString()
                }
        }
    }

    fun View.clicks(): Flow<Unit> = callbackFlow {
        this@clicks.setOnClickListener {
            this.offer(Unit)
        }
        awaitClose { this@clicks.setOnClickListener(null) }
    }
}

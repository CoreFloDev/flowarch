package com.example.flow_arch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {

    private val scope: CoroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.action_btn).clicks()
            .scan(0) { prev, _ -> prev + 1 }
            .flowOn(Dispatchers.Default)
            .onEach { findViewById<TextView>(R.id.action_tv).text = it.toString() }
            .launchIn(scope)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    fun View.clicks(): Flow<Unit> = callbackFlow {
        setOnClickListener { offer(Unit) }
        awaitClose { setOnClickListener(null) }
    }
}

package com.example.flow_arch.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.common.arch.ScreenView
import com.example.flow_arch.common.ui.LoadImage
import com.example.flow_arch.common.ui.theme.FlowArchTheme
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.main.arch.MovieState
import com.example.flow_arch.main.di.MainStateHolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity(), ScreenView<MainInput, MainOutput> {

    private lateinit var screen: Screen<MainInput, MainOutput>

    private val viewChannel = Channel<MainOutput.Display>()
    private val inputChannel = MutableSharedFlow<MainInput>(extraBufferCapacity = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowArchTheme {
                val state = viewChannel.consumeAsFlow().collectAsState(MainOutput.Display())
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("flow-arch") }
                        )
                    },
                    content = {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Button(onClick = {
                                        inputChannel.tryEmit(MainInput.Click)
                                }) {
                                    Text(text = "hello world!".uppercase())
                                }
                                Text(text = state.value.counter.toString())
                            }
                            when (val current = state.value.moviesState) {
                                is MovieState.Display -> {
                                    LazyColumn {
                                        items(current.list) { movie ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                                            ) {
                                                LoadImage(
                                                    url = movie.image,
                                                    modifier = Modifier.requiredSize(150.dp)
                                                )
                                                Column {
                                                    Text(
                                                        text = movie.title,
                                                        modifier = Modifier.padding(vertical = 4.dp),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(text = movie.overview)
                                                }

                                            }
                                        }
                                    }
                                }
                                MovieState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                MovieState.Retry -> {
                                    Button(onClick = {
                                        inputChannel.tryEmit(MainInput.RetryClicked)
                                    }) {
                                        Text(text = "RETRY")
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }

        screen = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(MainStateHolder::class.java)
            .screen

        screen.attach(this)
    }

    override fun onDestroy() {
        screen.detach()
        super.onDestroy()
    }

    override fun render(output: MainOutput) =
        when (output) {
            is MainOutput.Display -> {
                runBlocking {
                    launch {
                        viewChannel.send(output)
                    }
                }
                Unit
            }
            MainOutput.OpenNextScreen -> Unit //TODO
        }

    override fun inputs(): Flow<MainInput> = inputChannel
}

package com.example.flow_arch.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.flow_arch.common.arch.Screen
import com.example.flow_arch.common.arch.ScreenView
import com.example.flow_arch.common.ui.LoadImage
import com.example.flow_arch.common.ui.theme.FlowArchTheme
import com.example.flow_arch.main.arch.MainInput
import com.example.flow_arch.main.arch.MainOutput
import com.example.flow_arch.main.arch.MovieState
import com.example.flow_arch.main.di.MainStateHolder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ScreenView<MainInput, MainOutput> {

    private lateinit var screen: Screen<MainInput, MainOutput>

    private val inputChannel = MutableSharedFlow<MainInput>(extraBufferCapacity = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                setContent {
                    FlowArchTheme {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("flow-arch") }
                                )
                            },
                            content = {
                                Content(output = output, inputChannel = inputChannel)
                            },
                            bottomBar = {
                                BottomNavigation(backgroundColor = Color.White) {
                                    BottomNavigationItem(
                                        selected = false,
                                        onClick = { },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Place,
                                                contentDescription = "test"
                                            )
                                        },
                                        label = {
                                            Text(text = "test")
                                        })
                                    BottomNavigationItem(
                                        selected = true,
                                        onClick = { },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Email,
                                                contentDescription = "test2"
                                            )
                                        },
                                        label = {
                                            Text(text = "test2")
                                        })
                                    BottomNavigationItem(
                                        selected = false,
                                        onClick = { },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.AccountCircle,
                                                contentDescription = "test3"
                                            )
                                        },
                                        label = {
                                            Text(text = "test3")
                                        })
                                    BottomNavigationItem(
                                        selected = false,
                                        onClick = { },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.AccountCircle,
                                                contentDescription = "test4"
                                            )
                                        },
                                        label = {
                                            Text(text = "test4")
                                        })
                                    BottomNavigationItem(selected = false, onClick = { }, icon = {
                                        BadgedBox(badge = {
                                            Text(
                                                text = "20",
                                                fontSize = 10.sp,
                                                color = Color.White,
                                                modifier = Modifier
                                                    .background(Color.Red, shape = CircleShape)
                                                    .padding(start = 4.dp, end = 4.dp)
                                            )
//                                            CircularProgressIndicator(modifier = Modifier
//                                                .background(Color.Red, shape = CircleShape)
//                                                .padding(4.dp)
//                                                .width(8.dp)
//                                                .height(8.dp),
//                                                strokeWidth = 1.dp,
//                                                color = Color.White
//                                            )
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.ShoppingCart,
                                                contentDescription = "test5"
                                            )
                                        }

                                    }, label = {
                                        Text(text = "£45.56")
                                    })
                                }
                            }
                        )
                    }
                }
            }
            MainOutput.OpenNextScreen -> Unit //TODO
        }

    override fun inputs(): Flow<MainInput> = inputChannel
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Content(output: MainOutput.Display, inputChannel: MutableSharedFlow<MainInput>) {
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
            Text(text = output.counter.toString())
        }

        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState()


        TabRow(
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }
        ) {
            // Add tabs for all of our pages
            listOf("asd", "asdas", "hgjh", "dggfd", "dsfsdf").forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(count = 5, state = pagerState) { page ->
            // Our page content
            Text(
                text = "Page: $page",
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )

        when (val current = output.moviesState) {
            is MovieState.Display -> {
                LazyColumn {
                    items(current.list) { movie ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp)
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
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
}

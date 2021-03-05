/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.CountDownTimerTheme
import com.example.androiddevchallenge.ui.theme.countDownTimerTypography
import com.example.androiddevchallenge.util.getHour
import com.example.androiddevchallenge.util.getMillisecondsFromTimer
import com.example.androiddevchallenge.util.getMinute
import com.example.androiddevchallenge.util.getSecond
import com.example.androiddevchallenge.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.max

private val defaultSpacerSize = 16.dp

@Composable
private fun isNumberSelected(time: String) = time != "00h 00m 00s"

private val shouldDisplayEnterTime = mutableStateOf(true)
private val shouldDisplayCountDownTimer = mutableStateOf(false)

/**
 * Shows the entire screen.
 */
@Composable
fun Home(
    homeViewModel: HomeViewModel,
    onNumberSelected: (String) -> Unit,
    onNumberRemoved: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Timer")
                },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) {
        BodyContent(
            homeViewModel,
            onNumberSelected,
            onNumberRemoved
        )
    }
}

@Composable
fun BodyContent(
    homeViewModel: HomeViewModel,
    onNumberSelected: (String) -> Unit,
    onNumberRemoved: () -> Unit
) {
    Column {
        ShowCountDownTimer(
            isVisible = shouldDisplayCountDownTimer.value,
            homeViewModel = homeViewModel
        )
        ShowEnterTime(
            shouldDisplayEnterTime.value,
            homeViewModel,
            onNumberSelected,
            onNumberRemoved
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowEnterTime(
    isVisible: Boolean,
    homeViewModel: HomeViewModel,
    onNumberSelected: (String) -> Unit,
    onNumberRemoved: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            // Enters by sliding in from offset -fullHeight to 0.
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            // Exits by sliding out from offset 0 to -fullHeight.
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = defaultSpacerSize),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 40.sp
                                )
                            ) {
                                append(homeViewModel.time.getHour())
                            }
                            append("h   ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 40.sp
                                )
                            ) {
                                append(homeViewModel.time.getMinute())
                            }
                            append("m   ")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 40.sp
                                )
                            ) {
                                append(homeViewModel.time.getSecond())
                            }
                            append("s")
                        },
                        modifier = Modifier.padding(20.dp)
                    )

                    Button(
                        onClick = { onNumberRemoved() },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_baseline_backspace_24),
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
            item {
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("1") }
                    ) {
                        Text("1", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("2") }
                    ) {
                        Text("2", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("3") }
                    ) {
                        Text("3", fontSize = 30.sp)
                    }
                }
                Spacer(Modifier.height(defaultSpacerSize))
            }
            item {
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("4") }
                    ) {
                        Text("4", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("5") }
                    ) {
                        Text("5", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("6") }
                    ) {
                        Text("6", fontSize = 30.sp)
                    }
                }
                Spacer(Modifier.height(defaultSpacerSize))
            }
            item {
                Row {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("7") }
                    ) {
                        Text("7", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("8") }
                    ) {
                        Text("8", fontSize = 30.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = { onNumberSelected("9") }
                    ) {
                        Text("9", fontSize = 30.sp)
                    }
                }
                Spacer(Modifier.height(defaultSpacerSize))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onNumberSelected("0") }
                    ) {
                        Text("0", fontSize = 30.sp)
                    }
                }
                Spacer(Modifier.height(defaultSpacerSize))
                Spacer(Modifier.height(defaultSpacerSize))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShowPlayButton(
                        isVisible = isNumberSelected(time = homeViewModel.time),
                        homeViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowPlayButton(
    isVisible: Boolean,
    homeViewModel: HomeViewModel
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            // Enters by sliding in from offset -fullHeight to 0.
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            // Exits by sliding out from offset 0 to -fullHeight.
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        IconButton(
            onClick = {
                shouldDisplayEnterTime.value = false
                shouldDisplayCountDownTimer.value = true
                homeViewModel.isTimerRunning = true
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .size(60.dp)
                .background(
                    color = Color.Black,
                    shape = CircleShape
                ),
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Start Count Down Timer",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowCountDownTimer(
    homeViewModel: HomeViewModel,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            // Enters by sliding in from offset -fullHeight to 0.
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            // Exits by sliding out from offset 0 to -fullHeight.
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        if (isVisible) {
            if (!homeViewModel.isTimerRunning && homeViewModel.totalTime == 0L) {
                PresentDialog(homeViewModel = homeViewModel)
            }
            if (homeViewModel.isTimerRunning) {
                LaunchedEffect("Timer") {
                    while (isActive) {
                        delay(1000)
                        if (isActive) {
                            if (homeViewModel.isTimerRunning) {
                                homeViewModel.timeRemaining = homeViewModel.timeRemaining - 1000
                            }
                            if (homeViewModel.timeRemaining == 0L) {
                                homeViewModel.totalTime = 0
                                homeViewModel.isTimerRunning = false
                                homeViewModel.shouldDialogShow = true
                            }
                        }
                    }
                }
            }

            val target = max(
                homeViewModel.timeRemaining.toFloat() - 1000,
                0f
            ) / homeViewModel.totalTime

            val progress by animateFloatAsState(
                targetValue = target,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )

            // Animation
            val infiniteTransition = rememberInfiniteTransition()
            val timerColor by infiniteTransition.animateColor(
                initialValue = Color.Black,
                targetValue = Color.Red,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            val timerSize by infiniteTransition.animateFloat(
                initialValue = 40f,
                targetValue = 55f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                CircularProgressIndicator(
                    progress = 1F,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    strokeWidth = 8.dp
                )
                if (progress >= 0) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        color = Color.Black,
                        strokeWidth = 8.dp,
                    )
                }
                Box(
                    modifier = Modifier.aspectRatio(1.7f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row {
                        Text(
                            text = homeViewModel.getFormattedTimer(homeViewModel.timeRemaining),
                            style = countDownTimerTypography.h3,
                            color = if (!homeViewModel.isTimerRunning) timerColor else Color.Black,
                            fontSize = if (!homeViewModel.isTimerRunning) timerSize.sp else 40.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier.aspectRatio(0.8f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ButtonRow(
                            modifier = Modifier.layoutId("buttonRow"),
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row {
            IconButton(
                onClick = {
                    clearTimer(homeViewModel = homeViewModel)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(60.dp)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Count Down Timer",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.size(60.dp))
            IconButton(
                onClick = {
                    homeViewModel.isTimerRunning = !homeViewModel.isTimerRunning
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(60.dp)
                    .background(
                        color = Color.Black,
                        shape = CircleShape
                    ),
            ) {
                Icon(
                    imageVector = if (homeViewModel.isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (homeViewModel.isTimerRunning) "Pause Count Down Timer" else "Play Count Down Timer",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PresentDialog(homeViewModel: HomeViewModel) {
    AnimatedVisibility(
        visible = homeViewModel.shouldDialogShow,
        enter = slideInVertically(
            // Enters by sliding in from offset -fullHeight to 0.
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            // Exits by sliding out from offset 0 to -fullHeight.
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        if (homeViewModel.shouldDialogShow)
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    homeViewModel.shouldDialogShow = false
                },
                title = {
                    Text(
                        text = stringResource(R.string.str_time_up),
                        style = MaterialTheme.typography.h4
                    )
                },
                text = {
                    Text(
                        "Your count down timer is ${
                        homeViewModel.getFormattedTimer(
                            getMillisecondsFromTimer(homeViewModel.time)
                        )
                        }",
                        fontSize = 18.sp
                    )
                },
                confirmButton = {
                },
                dismissButton = {
                    Button(
                        onClick = {
                            clearTimer(homeViewModel = homeViewModel)
                            homeViewModel.shouldDialogShow = false
                        }
                    ) {
                        Text(stringResource(R.string.str_ok))
                    }
                }
            )
    }
}

fun clearTimer(homeViewModel: HomeViewModel) {
    homeViewModel.time = "00h 00m 00s"
    homeViewModel.totalTime = getMillisecondsFromTimer(homeViewModel.time)
    homeViewModel.timeRemaining =
        getMillisecondsFromTimer(homeViewModel.time)
    shouldDisplayEnterTime.value = true
    shouldDisplayCountDownTimer.value = false
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    CountDownTimerTheme {
        Home(homeViewModel = viewModel(), {}, {})
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    CountDownTimerTheme(darkTheme = true) {
        Home(homeViewModel = viewModel(), {}, {})
    }
}

@Preview("Light Theme Button Row", widthDp = 360, heightDp = 640)
@Composable
fun LightPreviewRow() {
    ButtonRow(modifier = Modifier, homeViewModel = viewModel())
}

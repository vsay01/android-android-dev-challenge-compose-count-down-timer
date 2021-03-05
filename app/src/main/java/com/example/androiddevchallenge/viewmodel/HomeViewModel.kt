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
package com.example.androiddevchallenge.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.util.getFirstDigitOfHour
import com.example.androiddevchallenge.util.getFirstDigitOfMinute
import com.example.androiddevchallenge.util.getFirstDigitOfSecond
import com.example.androiddevchallenge.util.getMillisecondsFromTimer
import com.example.androiddevchallenge.util.getSecondDigitOfHour
import com.example.androiddevchallenge.util.getSecondDigitOfMinute
import com.example.androiddevchallenge.util.getSecondDigitOfSecond
import com.example.androiddevchallenge.util.replaceFirstDigitOfHour
import com.example.androiddevchallenge.util.replaceFirstDigitOfMinute
import com.example.androiddevchallenge.util.replaceFirstDigitOfSecond
import com.example.androiddevchallenge.util.replaceSecondDigitOfHour
import com.example.androiddevchallenge.util.replaceSecondDigitOfMinute
import com.example.androiddevchallenge.util.replaceSecondDigitOfSecond

class HomeViewModel : ViewModel() {
    // state: todoItems
    var time: String by mutableStateOf(String())
    var totalTime: Long by mutableStateOf(0L)
    var timeRemaining: Long by mutableStateOf(0L)
    var isTimerRunning: Boolean by mutableStateOf(true)
    var shouldDialogShow: Boolean by mutableStateOf(false)

    init {
        time = "00h 00m 00s"
    }

    fun getFormattedTimer(timeMilliseconds: Long): String {
        val seconds = (timeMilliseconds / 1000) % 60
        val minutes = ((timeMilliseconds / 1000) / 60) % 60
        val hours = ((timeMilliseconds / 1000) / 60) / 60
        if (seconds <= 0L && minutes <= 0L && hours <= 0L) {
            return "0"
        }
        return if (hours == 0L && minutes == 0L) {
            "%02ds".format(seconds)
        } else if (hours == 0L) {
            "%02dm:%02ds".format(minutes, seconds)
        } else {
            "%02dh:%02dm:%02ds".format(hours, minutes, seconds)
        }
    }

    // event: time added 00h 00m 00s
    fun addTime(number: String) {
        when {
            time.getFirstDigitOfHour() == "0" -> {
                time = time.replaceFirstDigitOfHour(time.getSecondDigitOfHour())
                time = time.replaceSecondDigitOfHour(time.getFirstDigitOfMinute())
                time = time.replaceFirstDigitOfMinute(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfSecond())
                time = time.replaceSecondDigitOfSecond(number)
            }
            time.getSecondDigitOfHour() == "0" -> {
                time = time.replaceSecondDigitOfHour(time.getFirstDigitOfMinute())
                time = time.replaceFirstDigitOfMinute(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfSecond())
                time = time.replaceSecondDigitOfSecond(number)
            }
            time.getFirstDigitOfMinute() == "0" -> {
                time = time.replaceFirstDigitOfMinute(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfSecond())
                time = time.replaceSecondDigitOfSecond(number)
            }
            time.getSecondDigitOfMinute() == "0" -> {
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfSecond())
                time = time.replaceSecondDigitOfSecond(number)
            }
            time.getFirstDigitOfSecond() == "0" -> {
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfSecond())
                time = time.replaceSecondDigitOfSecond(number)
            }
            time.getSecondDigitOfSecond() == "0" -> {
                time = time.replaceSecondDigitOfSecond(number)
            }
        }
        totalTime = getMillisecondsFromTimer(time)
        timeRemaining = getMillisecondsFromTimer(time)
    }

    // event: remove time
    fun removeTime() {
        when {
            time.getFirstDigitOfHour() != "0" -> {
                time = time.replaceSecondDigitOfSecond(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfMinute())
                time = time.replaceFirstDigitOfMinute(time.getSecondDigitOfHour())
                time = time.replaceSecondDigitOfHour(time.getFirstDigitOfHour())
                time = time.replaceFirstDigitOfHour("0")
            }
            time.getSecondDigitOfHour() != "0" -> {
                time = time.replaceSecondDigitOfSecond(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfMinute())
                time = time.replaceFirstDigitOfMinute(time.getSecondDigitOfHour())
                time = time.replaceSecondDigitOfHour("0")
                time = time.replaceFirstDigitOfHour("0")
            }
            time.getFirstDigitOfMinute() != "0" -> {
                time = time.replaceSecondDigitOfSecond(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute(time.getFirstDigitOfMinute())
                time = time.replaceFirstDigitOfMinute("0")
                time = time.replaceSecondDigitOfHour("0")
                time = time.replaceFirstDigitOfHour("0")
            }
            time.getSecondDigitOfMinute() != "0" -> {
                time = time.replaceSecondDigitOfSecond(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond(time.getSecondDigitOfMinute())
                time = time.replaceSecondDigitOfMinute("0")
                time = time.replaceFirstDigitOfMinute("0")
                time = time.replaceSecondDigitOfHour("0")
                time = time.replaceFirstDigitOfHour("0")
            }
            time.getFirstDigitOfSecond() != "0" -> {
                time = time.replaceSecondDigitOfSecond(time.getFirstDigitOfSecond())
                time = time.replaceFirstDigitOfSecond("0")
                time = time.replaceSecondDigitOfMinute("0")
                time = time.replaceFirstDigitOfMinute("0")
                time = time.replaceSecondDigitOfHour("0")
                time = time.replaceFirstDigitOfHour("0")
            }
            time.getSecondDigitOfSecond() != "0" -> {
                time = time.replaceSecondDigitOfSecond("0")
                time = time.replaceFirstDigitOfSecond("0")
                time = time.replaceSecondDigitOfMinute("0")
                time = time.replaceFirstDigitOfMinute("0")
                time = time.replaceSecondDigitOfHour("0")
                time = time.replaceFirstDigitOfHour("0")
            }
        }
        totalTime = getMillisecondsFromTimer(time)
        timeRemaining = getMillisecondsFromTimer(time)
    }
}

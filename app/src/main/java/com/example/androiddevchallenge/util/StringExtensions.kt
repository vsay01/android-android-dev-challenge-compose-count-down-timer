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
package com.example.androiddevchallenge.util

// 00h 00m 00s
fun String.getFirstDigitOfSecond() = substring(startIndex = 8, endIndex = 9)

fun String.getSecondDigitOfSecond() = substring(startIndex = 9, endIndex = 10)

fun String.getFirstDigitOfMinute() = substring(startIndex = 4, endIndex = 5)

fun String.getSecondDigitOfMinute() = substring(startIndex = 5, endIndex = 6)

fun String.getFirstDigitOfHour() = substring(startIndex = 0, endIndex = 1)

fun String.getSecondDigitOfHour() = substring(startIndex = 1, endIndex = 2)

// ========= //

fun String.getSecond() = substring(8, 10)

fun String.getMinute() = substring(4, 6)

fun String.getHour() = substring(0, 2)

// ========= //
fun String.replaceFirstDigitOfSecond(number: String) = replaceRange(startIndex = 8, endIndex = 9, number)

fun String.replaceSecondDigitOfSecond(number: String) = replaceRange(startIndex = 9, endIndex = 10, number)

fun String.replaceFirstDigitOfMinute(number: String) = replaceRange(startIndex = 4, endIndex = 5, number)

fun String.replaceSecondDigitOfMinute(number: String) = replaceRange(startIndex = 5, endIndex = 6, number)

fun String.replaceFirstDigitOfHour(number: String) = replaceRange(startIndex = 0, endIndex = 1, number)

fun String.replaceSecondDigitOfHour(number: String) = replaceRange(startIndex = 1, endIndex = 2, number)

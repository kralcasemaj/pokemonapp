package com.example.pokemonlist.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

sealed class AsyncState<T> {
    class Initialised<T> : AsyncState<T>()
    class Loading<T> : AsyncState<T>()
    data class Error<T>(val error: Throwable) : AsyncState<T>()
    data class Result<T>(val result: T) : AsyncState<T>()
}

@Composable
fun <T> observe(data: LiveData<T>): T? {
    var result by remember { mutableStateOf(data.value) }
    val observer = remember { Observer<T> { result = it } }

    DisposableEffect(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    return result
}

package com.example.pokemonlist.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.pokemonlist.R

@Composable
fun PokeBallBackground() {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.offset(x = 90.dp, y = (-70).dp)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .width(240.dp)
                .height(240.dp)
        ) {
            PokeBallLarge(
                colorResource(
                    R.color.grey_100
                )
            )
        }
    }
}

package com.example.pokemonlist.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pokemonlist.R

@Composable
fun PokeBallLarge(tint: Color, opacity: Float = 1f, modifier: Modifier = Modifier) {
    PokeBall(tint, opacity, R.drawable.pokeball, modifier)
}

@Composable
fun PokeBallSmall(tint: Color, opacity: Float = 1f, modifier: Modifier = Modifier) {
    PokeBall(tint, opacity, R.drawable.pokeball_s, modifier)
}

@Composable
private fun PokeBall(tint: Color, opacity: Float, imageResId: Int, modifier: Modifier = Modifier) {
    LoadImageFromResId(imageResId, tint, opacity, modifier = modifier)
}

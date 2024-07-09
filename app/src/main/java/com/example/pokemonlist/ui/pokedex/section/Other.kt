package com.example.pokemonlist.ui.pokedex.section

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pokemonlist.model.pokemon.Pokemon

@Composable
fun Other(pokemon: Pokemon) {
    Column {
        Text(
            text = "Weight: " + pokemon.weight,
            color = Color.Black,
        )
        Text(
            text = "Base XP: " + pokemon.baseExperience,
            color = Color.Black,
        )
    }
}

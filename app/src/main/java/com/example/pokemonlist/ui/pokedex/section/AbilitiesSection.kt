package com.example.pokemonlist.ui.pokedex.section

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pokemonlist.model.pokemon.Pokemon
import java.util.Locale

@Composable
fun AbilitiesSection(pokemon: Pokemon) {
    val text = pokemon.abilities.map {
        it.ability?.name?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.US
            ) else it.toString()
        }
            ?: ""
    }.toList()
        .joinToString(", ")
    Text(text = text, color = Color.Black)
}

package com.example.pokemonlist.pokedex.section

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.pokemonlist.data.pokemon.Pokemon

@Composable
fun AbilitiesSection(pokemon: Pokemon) {
    val text = pokemon.abilities.map { it.ability?.name }.joinToString { "\n" }
    Text(text)
}

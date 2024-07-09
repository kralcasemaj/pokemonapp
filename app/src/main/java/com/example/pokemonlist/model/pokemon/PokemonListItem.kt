package com.example.pokemonlist.model.pokemon

import java.util.Locale

data class PokemonListItem(
    val name: String,
    val url: String


) {
    fun isMatchWithQuery(queryString: String): Boolean {
        val matchResult = listOf(
            name.lowercase(Locale.US), "${name.first()}"
        )

        return matchResult.any {
            it.contains(queryString.lowercase(Locale.US), true)
        }
    }
}

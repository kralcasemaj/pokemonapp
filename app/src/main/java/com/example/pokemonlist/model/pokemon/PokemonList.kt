package com.example.pokemonlist.model.pokemon

data class PokemonList(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonListItem>
)

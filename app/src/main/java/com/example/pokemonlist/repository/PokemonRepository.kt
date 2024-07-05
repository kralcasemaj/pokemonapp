package com.example.pokemonlist.repository

import com.example.pokemonlist.api.PokemonAPI

class PokemonRepository(private val api: PokemonAPI) {
    suspend fun getPokemonList() = api.getPokemonList()
    suspend fun getPokemonDetails(id: Int) = api.getPokemonDetails(id)
}

package com.example.pokemonlist.api

import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.data.pokemon.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonAPI {
    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
    ): Response<PokemonList>

    @GET
    suspend fun getPokemonDetails(@Url url: String): Response<Pokemon>
}

package com.example.pokemonlist.api

import android.content.Context
import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.data.pokemon.PokemonList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Properties

interface PokemonAPI {
    @GET("api/v2/pokemon/?limit={limit}&offset={offset}")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
    ): Response<PokemonList>

    @GET("api/v2/pokemon/{id}/")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<Pokemon>

    companion object {
        private var instance: PokemonAPI? = null

        fun getInstance(baseUrl: String): PokemonAPI {
            val currentInstance = instance
            return if (currentInstance == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val newInstance = retrofit.create(PokemonAPI::class.java)
                instance = newInstance
                newInstance
            } else {
                currentInstance
            }
        }

        fun getApiBaseUri(context: Context): String {
            return getProperty(
                "api.properties",
                "apiBaseUri",
                context
            )
        }

        private fun getProperty(propertiesFile: String, key: String, context: Context): String {
            val properties = Properties()
            val assetManager = context.assets
            assetManager.open(propertiesFile).use { inputStream ->
                properties.load(inputStream)
                return properties.getProperty(key)
            }
        }
    }
}

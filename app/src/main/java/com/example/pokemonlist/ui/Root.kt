package com.example.pokemonlist.ui

import androidx.compose.runtime.Composable
import com.example.pokemonlist.model.pokemon.PokemonListItem
import com.example.pokemonlist.ui.pokedex.PokemonDetails
import com.example.pokemonlist.ui.pokedex.PokemonList
import com.github.zsoltk.compose.router.Router

interface Root {

    sealed class Routing {
        data object PokemonList : Routing()
        data class PokemonDetails(val pokemon: PokemonListItem) : Routing()
    }

    companion object {
        @Composable
        fun Content(defaultRouting: Routing = Routing.PokemonList) {
            Router(defaultRouting) { backStack ->
                when (val routing = backStack.last()) {
                    is Routing.PokemonList -> PokemonList.Content(
                        onPokemonSelected = { backStack.push(Routing.PokemonDetails(it)) }
                    )

                    is Routing.PokemonDetails -> PokemonDetails.Content(routing.pokemon)
                }
            }
        }
    }
}

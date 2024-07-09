package com.example.pokemonlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.api.PokemonAPI
import com.example.pokemonlist.model.pokemon.Pokemon
import com.example.pokemonlist.model.pokemon.PokemonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonAPI: PokemonAPI
) : ViewModel() {
    enum class State {
        Initialised,
        Loading,
        Error,
        Result
    }

    val errorMessage = MutableStateFlow<String?>(null)
    val pokemonList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    private val pokemonListCache: MutableList<PokemonListItem> = ArrayList()
    val pokemonDetails =
        MutableStateFlow<ConcurrentHashMap<String, Pokemon>>(ConcurrentHashMap())
    private val pokemonDetailsCache = ConcurrentHashMap<String, Pokemon>()
    val loadingState = MutableStateFlow(State.Initialised)
    private var job: Job? = null

    private val searchQueryStateFlow = MutableStateFlow("")
    val searchQueryText = searchQueryStateFlow.asStateFlow()

    private var pokemonListStateFlow = MutableStateFlow(pokemonListCache)
    var pokemons = searchQueryText
        .combine(pokemonListStateFlow) { query, pokemon ->
            if (query.isBlank()) {
                pokemon
            } else {
                pokemon.filter { it.isMatchWithQuery(query) }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            pokemonListStateFlow.value
        )

    fun onQueryTextChanged(query: String) {
        searchQueryStateFlow.value = query
    }

    fun getPokemonList(reload: Boolean = false) {
        if (pokemonListCache.isEmpty() || reload) {
            job = viewModelScope.launch {
                loadingState.value = State.Loading
                val response = pokemonAPI.getPokemonList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonListCache.clear()
                            pokemonListCache.addAll(it.results)
                            pokemonList.value = pokemonListCache
                            loadingState.value = State.Result
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    fun getPokemonDetails(pokemonListItem: PokemonListItem, reload: Boolean = false) {
        if (!pokemonDetailsCache.contains(pokemonListItem.name) || reload) {
            job = viewModelScope.launch {
                val response = pokemonAPI.getPokemonDetails(pokemonListItem.url)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonDetailsCache[pokemonListItem.name] = it
                            pokemonDetails.value = pokemonDetailsCache
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loadingState.value = State.Error
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        loadingState.value = State.Initialised
    }
}

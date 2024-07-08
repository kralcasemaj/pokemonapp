package com.example.pokemonlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.api.PokemonAPI
import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.data.pokemon.PokemonListItem
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

    val errorMessageLiveData = MutableLiveData<String>()
    val pokemonListLiveData = MutableLiveData<List<PokemonListItem>>()
    private val pokemonList: MutableList<PokemonListItem> = ArrayList()
    val pokemonDetailsLiveData =
        MutableLiveData<ConcurrentHashMap<String, Pokemon>>(ConcurrentHashMap())
    private val pokemonDetailsCache = ConcurrentHashMap<String, Pokemon>()
    val loadingState = MutableLiveData(State.Initialised)
    private var job: Job? = null

    private val searchQueryStateFlow = MutableStateFlow("")
    val searchQueryText = searchQueryStateFlow.asStateFlow()

    private var pokemonListStateFlow = MutableStateFlow(pokemonList)
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
        if (pokemonList.isEmpty() || reload) {
            job = viewModelScope.launch {
                loadingState.postValue(State.Loading)
                val response = pokemonAPI.getPokemonList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonList.clear()
                            pokemonList.addAll(it.results)
                            pokemonListLiveData.postValue(pokemonList)
                            loadingState.postValue(State.Result)
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
                            pokemonDetailsLiveData.postValue(pokemonDetailsCache)
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessageLiveData.value = message
        loadingState.postValue(State.Error)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        loadingState.postValue(State.Initialised)
    }
}

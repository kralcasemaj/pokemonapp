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

    val errorMessage = MutableLiveData<String>()
    val pokemonList = MutableLiveData<List<PokemonListItem>>()
    val _pokemonList: MutableList<PokemonListItem> = ArrayList()
    val pokemonDetails = MutableLiveData<ConcurrentHashMap<String, Pokemon>>(ConcurrentHashMap())
    private val pokemonMap = ConcurrentHashMap<String, Pokemon>()
    val state = MutableLiveData(State.Initialised)
    private var job: Job? = null

    private val _queryText = MutableStateFlow("")
    val queryText = _queryText.asStateFlow()

    private var _pokemons = MutableStateFlow(_pokemonList)
    var pokemons = queryText
        .combine(_pokemons) { query, pokemon ->
            if (query.isBlank()) {
                pokemon
            } else {
                pokemon.filter { it.isMatchWithQuery(query) }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _pokemons.value
        )

    fun onQueryTextChanged(query: String) {
        _queryText.value = query
    }

    fun getPokemonList(reload: Boolean = false) {
        if (_pokemonList.isEmpty() || reload) {
            job = viewModelScope.launch {
                state.postValue(State.Loading)
                val response = pokemonAPI.getPokemonList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _pokemonList.clear()
                            _pokemonList.addAll(it.results)
                            pokemonList.postValue(_pokemonList)
                            state.postValue(State.Result)
                        }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    fun getPokemonDetails(pokemonListItem: PokemonListItem, reload: Boolean = false) {
        if (!pokemonMap.contains(pokemonListItem.name) || reload) {
            job = viewModelScope.launch {
                val response = pokemonAPI.getPokemonDetails(pokemonListItem.url)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonMap[pokemonListItem.name] = it
                            pokemonDetails.postValue(pokemonMap)
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
        state.postValue(State.Error)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        state.postValue(State.Initialised)
    }
}

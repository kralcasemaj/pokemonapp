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
    val pokemonDetails = MutableLiveData<ConcurrentHashMap<String, Pokemon>>(ConcurrentHashMap())
    private val pokemonMap = ConcurrentHashMap<String, Pokemon>()
    val state = MutableLiveData(State.Initialised)
    var job: Job? = null

    fun getPokemonList(reload: Boolean = false) {
        if (pokemonList.value.isNullOrEmpty() || reload) {
            job = viewModelScope.launch {
                state.postValue(State.Loading)
                val response = pokemonAPI.getPokemonList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            pokemonList.postValue(it.results)
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

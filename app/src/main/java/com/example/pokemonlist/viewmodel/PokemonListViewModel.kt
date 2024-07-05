package com.example.pokemonlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View model for [com.example.pokemonlist.PokemonListFragment]
 */
class PokemonListViewModel(
    private val repository: PokemonRepository,
): ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val pokemonList = MutableLiveData<List<Pokemon>>()
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    fun getPokemonList() {
        job = viewModelScope.launch {
            val response = repository.getPokemonList()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        pokemonList.postValue(it.results)
                        loading.value = false
                    }
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
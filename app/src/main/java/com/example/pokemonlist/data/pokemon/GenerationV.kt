package com.example.pokemonlist.data.pokemon

import com.google.gson.annotations.SerializedName


data class GenerationV(

    @SerializedName("black-white") var blackWhite: BlackWhite? = BlackWhite()

)
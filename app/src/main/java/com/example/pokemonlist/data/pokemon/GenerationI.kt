package com.example.pokemonlist.data.pokemon

import com.google.gson.annotations.SerializedName


data class GenerationI(

    @SerializedName("red-blue") var redBlue: RedBlue? = RedBlue(),
    @SerializedName("yellow") var yellow: Yellow? = Yellow()

)
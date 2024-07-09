package com.example.pokemonlist.model.pokemon

import com.google.gson.annotations.SerializedName


data class GenerationVI(

    @SerializedName("omegaruby-alphasapphire") var omegarubyAlphasapphire: OmegarubyAlphaSapphire? = OmegarubyAlphaSapphire(),
    @SerializedName("x-y") var xY: Xy? = Xy()

)
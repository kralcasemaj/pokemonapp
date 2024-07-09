package com.example.pokemonlist.model.pokemon

import com.google.gson.annotations.SerializedName


data class Stats(

    @SerializedName("base_stat") var baseStat: Int? = null,
    @SerializedName("effort") var effort: Int? = null,
    @SerializedName("stat") var stat: Stat? = Stat()

)
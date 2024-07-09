package com.example.pokemonlist.model.pokemon

import com.google.gson.annotations.SerializedName


data class MoveLearnMethod(

    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null

)
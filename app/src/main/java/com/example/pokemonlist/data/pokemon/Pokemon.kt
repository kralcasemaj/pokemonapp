package com.example.pokemonlist.data.pokemon

import com.example.pokemonlist.R
import com.google.gson.annotations.SerializedName


data class Pokemon(

    @SerializedName("abilities") var abilities: ArrayList<Abilities> = arrayListOf(),
    @SerializedName("base_experience") var baseExperience: Int? = null,
    @SerializedName("cries") var cries: Cries? = Cries(),
    @SerializedName("forms") var forms: ArrayList<Forms> = arrayListOf(),
    @SerializedName("game_indices") var gameIndices: ArrayList<GameIndices> = arrayListOf(),
    @SerializedName("height") var height: Int? = null,
    @SerializedName("held_items") var heldItems: ArrayList<String> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_default") var isDefault: Boolean? = null,
    @SerializedName("location_area_encounters") var locationAreaEncounters: String? = null,
    @SerializedName("moves") var moves: ArrayList<Moves> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") var order: Int? = null,
    @SerializedName("past_abilities") var pastAbilities: ArrayList<String> = arrayListOf(),
    @SerializedName("past_types") var pastTypes: ArrayList<String> = arrayListOf(),
    @SerializedName("species") var species: Species? = Species(),
    @SerializedName("sprites") var sprites: Sprites? = Sprites(),
    @SerializedName("stats") var stats: ArrayList<Stats> = arrayListOf(),
    @SerializedName("types") var types: ArrayList<Types> = arrayListOf(),
    @SerializedName("weight") var weight: Int? = null
)

fun Pokemon.color(): Int {
    val type = types.elementAtOrNull(0)?.type?.name

    return when (type?.lowercase()) {
        "grass", "bug" -> R.color.poke_light_teal
        "fire" -> R.color.poke_light_red
        "water", "fighting", "normal" -> R.color.poke_light_blue
        "electric", "psychic" -> R.color.poke_light_yellow
        "poison", "ghost" -> R.color.poke_light_purple
        "ground", "rock" -> R.color.poke_light_brown
        "dark" -> R.color.poke_black
        else -> return R.color.poke_light_blue
    }
}

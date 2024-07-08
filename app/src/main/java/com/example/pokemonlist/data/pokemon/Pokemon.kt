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
    @SerializedName("held_items") var heldItems: ArrayList<HeldItem> = arrayListOf(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_default") var isDefault: Boolean? = null,
    @SerializedName("location_area_encounters") var locationAreaEncounters: String? = null,
    @SerializedName("moves") var moves: ArrayList<Moves> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("order") var order: Int? = null,
    @SerializedName("past_abilities") var pastAbilities: ArrayList<PastAbility> = arrayListOf(),
    @SerializedName("past_types") var pastTypes: ArrayList<PastType> = arrayListOf(),
    @SerializedName("species") var species: Species? = Species(),
    @SerializedName("sprites") var sprites: Sprites? = Sprites(),
    @SerializedName("stats") var stats: ArrayList<Stats> = arrayListOf(),
    @SerializedName("types") var types: ArrayList<Types> = arrayListOf(),
    @SerializedName("weight") var weight: Int? = null
)

fun Pokemon.color(): Int {
    val type = types.elementAtOrNull(0)?.type?.name

    return when (type?.lowercase()) {
        "grass" -> R.color.poke_light_teal
        "bug" -> R.color.poke_bug_green
        "fire" -> R.color.poke_light_red
        "normal" -> R.color.poke_light_grey
        "fighting" -> R.color.poke_light_orange
        "water" -> R.color.poke_light_blue
        "electric" -> R.color.poke_light_yellow
        "psychic" -> R.color.poke_dark_pink
        "poison" -> R.color.poke_light_purple
        "ghost" -> R.color.poke_ghost_purple
        "ground" -> R.color.poke_light_brown
        "rock" -> R.color.poke_light_beige
        "dark" -> R.color.poke_black
        else -> return R.color.poke_light_blue
    }
}

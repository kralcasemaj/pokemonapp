package com.example.pokemonlist.pokedex.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokemonlist.R
import com.example.pokemonlist.data.pokemon.Pokemon

@Composable
fun MovesSection(pokemon: Pokemon) {
    val moveLevelMap = HashMap<String, Int>()

    val moves =
        pokemon.moves.filter { move ->
            move.versionGroupDetails.any { vg ->
                vg.versionGroup?.name.equals(
                    "red-blue"
                ) && vg.moveLearnMethod?.name.equals("level-up")
            }
        }.sortedBy {
            it.versionGroupDetails.firstOrNull { vg ->
                vg.versionGroup?.name.equals(
                    "red-blue"
                ) && vg.moveLearnMethod?.name.equals("level-up")
            }?.levelLearnedAt ?: 0
        }
    Column {
        Text(
            text = "Moves learned by level in Red/Blue: ",
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(moves.size) {
                val move = moves[it]
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = move.move?.name?.capitalize(androidx.compose.ui.text.intl.Locale.current)
                            ?: "",
                        Modifier
                            .padding(end = 16.dp, bottom = 8.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = colorResource(R.color.grey_900)
                        )
                    )
                    Text(
                        text = move.versionGroupDetails.firstOrNull { vg ->
                            vg.versionGroup?.name.equals(
                                "red-blue"
                            )
                        }?.levelLearnedAt.toString(),
                        Modifier
                            .padding(end = 16.dp, bottom = 8.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


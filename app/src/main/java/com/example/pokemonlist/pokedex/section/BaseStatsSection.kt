package com.example.pokemonlist.pokedex.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokemonlist.R
import com.example.pokemonlist.data.pokemon.Pokemon

data class Stat(
    val label: String,
    val value: Int?,
    val max: Int = 100
) {
    val progress: Float =
        1f * (value ?: 0) / max
}


@Composable
fun BaseStatsSection(pokemon: Pokemon) {
    val stats = listOf(
        Stat("HP", pokemon.stats.firstOrNull { it.stat?.name.equals("hp") }?.baseStat),
        Stat("Attack", pokemon.stats.firstOrNull { it.stat?.name.equals("attack") }?.baseStat),
        Stat("Defense", pokemon.stats.firstOrNull { it.stat?.name.equals("defense") }?.baseStat),
        Stat(
            "Sp. Atk",
            pokemon.stats.firstOrNull { it.stat?.name.equals("special-attack") }?.baseStat
        ),
        Stat(
            "Sp. Def",
            pokemon.stats.firstOrNull { it.stat?.name.equals("special-defense") }?.baseStat
        ),
        Stat("Speed", pokemon.stats.firstOrNull { it.stat?.name.equals("speed") }?.baseStat),
        Stat("Total", pokemon.stats.sumOf { it.baseStat ?: 0 }, 600)
    )
    StatsTable(stats)
}

@Composable
private fun StatsTable(stats: List<Stat>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(stats.size) {
            val stat = stats[it]
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = stat.label,
                    Modifier
                        .padding(end = 16.dp, bottom = 8.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colorResource(R.color.grey_900)
                    )
                )
                Text(
                    text = stat.value.toString(),
                    Modifier
                        .padding(end = 16.dp, bottom = 8.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .weight(0.6f)
                ) {
                    LinearProgressIndicator(
                        progress = { stat.progress },
                        color = Color.Red,
                    )
                }
            }
        }
    }
}

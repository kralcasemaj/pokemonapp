package com.example.pokemonlist.pokedex

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonlist.R
import com.example.pokemonlist.appFontFamily
import com.example.pokemonlist.common.LoadImageFromResId
import com.example.pokemonlist.common.LoadImageFromSvgUrl
import com.example.pokemonlist.common.PokeBallLarge
import com.example.pokemonlist.common.PokemonTypeLabels
import com.example.pokemonlist.common.Title
import com.example.pokemonlist.common.TypeLabelMetrics.Companion.MEDIUM
import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.data.pokemon.PokemonListItem
import com.example.pokemonlist.data.pokemon.color
import com.example.pokemonlist.pokedex.section.AbilitiesSection
import com.example.pokemonlist.pokedex.section.BaseStatsSection
import com.example.pokemonlist.pokedex.section.EvolutionSection
import com.example.pokemonlist.pokedex.section.MovesSection
import com.example.pokemonlist.viewmodel.PokemonViewModel

interface PokemonDetails {

    companion object {
        @Composable
        fun Content(
            pokemonListIem: PokemonListItem,
            viewModel: PokemonViewModel = viewModel()
        ) {
            val pokemon by viewModel.pokemonDetails.observeAsState()

            viewModel.getPokemonDetails(pokemonListIem.url)

            pokemon?.let {
                Surface(color = colorResource(it.color())) {
                    Box {
                        RoundedRectangleDecoration()
                        DottedDecoration()
                        RotatingPokeBall()
                        HeaderLeft(it)
                        HeaderRight(it)
                        CardContent(it)
                        PokemonImage(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoundedRectangleDecoration() {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.offset(x = (-60).dp, y = (-50).dp)
    ) {
        Surface(
            color = Color(0x22FFFFFF), shape = RoundedCornerShape(32.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .rotate(-20f)
            )
        }
    }

}

@Composable
private fun DottedDecoration() {
    Box(
        contentAlignment = Alignment.TopEnd, modifier = Modifier
            .padding(
                top = 4.dp, end = 100.dp
            )
            .width(63.dp)
            .height(34.dp)
    ) {
        LoadImageFromResId(imageResId = R.drawable.dotted, opacity = 0.3f)
    }
}

@Composable
private fun RotatingPokeBall() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(top = 140.dp)
            .size(200.dp)
    ) {
        val label = "rotatePokeball"
        val infiniteTransition = rememberInfiniteTransition(label)
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = androidx.compose.animation.core.LinearEasing)
            ),
            label = label
        )
        val modifier = Modifier
            .graphicsLayer {
                rotationZ = angle
            }
        PokeBallLarge(
            tint = colorResource(R.color.grey_100), opacity = 0.25f, modifier = modifier
        )
    }
}

@Composable
private fun HeaderRight(pokemon: Pokemon) {
    Box(
        contentAlignment = Alignment.TopEnd, modifier = Modifier
            .padding(top = 52.dp)
            .padding(32.dp)
    ) {
        Column {
            Text(
                textAlign = TextAlign.End, text = pokemon.id.toString(), style = TextStyle(
                    fontFamily = appFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
private fun HeaderLeft(pokemon: Pokemon) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .padding(top = 40.dp)
            .padding(32.dp)
    ) {
        Column {
            Title(
                text = pokemon.name ?: "", color = Color.White
            )

            pokemon.types.map { type -> type.type?.name ?: "" }.let {
                Row {
                    PokemonTypeLabels(it, MEDIUM)
                }
            }
        }
    }
}

private enum class Sections(val title: String) {
    Abilities("Abilities"), BaseStats("Base stats"), Evolution("Evolution"), Moves("Moves")
}

@Composable
private fun CardContent(pokemon: Pokemon) {
    Box(
        contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(top = 300.dp)
    ) {
        Surface(shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                val sectionTitles = Sections.entries.map { it.title }
                var section by remember { mutableStateOf(Sections.BaseStats) }
                TabRow(selectedTabIndex = section.ordinal) {
                    sectionTitles.forEachIndexed { index, text ->
                        Tab(text = { Text(text) },
                            selected = section.ordinal == index,
                            onClick = { section = Sections.entries[index] })
                    }
                }

                Box(modifier = Modifier.padding(24.dp)) {
                    when (section) {
                        Sections.Abilities -> AbilitiesSection(pokemon)
                        Sections.BaseStats -> BaseStatsSection(pokemon)
                        Sections.Evolution -> EvolutionSection(pokemon)
                        Sections.Moves -> MovesSection(pokemon)
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonImage(pokemon: Pokemon) {
    pokemon.sprites?.other?.dreamWorld?.frontDefault?.let { imageUrl ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(top = 140.dp)
                .size(200.dp)
        ) {
            LoadImageFromSvgUrl(imageUrl)
        }
    }
}

package com.example.pokemonlist.pokedex

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonlist.R
import com.example.pokemonlist.appFontFamily
import com.example.pokemonlist.common.LoadImageFromSvgUrl
import com.example.pokemonlist.common.PokeBallBackground
import com.example.pokemonlist.common.PokeBallSmall
import com.example.pokemonlist.common.PokemonTypeLabels
import com.example.pokemonlist.common.Title
import com.example.pokemonlist.common.TypeLabelMetrics.Companion.SMALL
import com.example.pokemonlist.data.pokemon.Pokemon
import com.example.pokemonlist.data.pokemon.PokemonListItem
import com.example.pokemonlist.data.pokemon.color
import com.example.pokemonlist.viewmodel.PokemonViewModel

interface PokemonList {

    companion object {
        @Composable
        fun Content(
            onPokemonSelected: (PokemonListItem) -> Unit, viewModel: PokemonViewModel = viewModel()
        ) {
            val viewModelState by viewModel.state.observeAsState()
            val errorMessage by viewModel.errorMessage.observeAsState()
            val pokemonList by viewModel.pokemonList.observeAsState()

            viewModel.getPokemonList()

            viewModelState?.let { state ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(color = MaterialTheme.colorScheme.surface) {
                        PokeBallBackground()
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Crossfade(targetState = state, label = "pokemonList") {
                            when (it) {
                                PokemonViewModel.State.Initialised, PokemonViewModel.State.Loading -> LoadingView()
                                PokemonViewModel.State.Error -> ErrorView(errorMessage!!)
                                PokemonViewModel.State.Result -> ContentView(
                                    pokemonList!!, onPokemonSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box {
        val label = "LoadingView"
        val infiniteTransition = rememberInfiniteTransition(label)
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F, targetValue = 360F, animationSpec = infiniteRepeatable(
                animation = tween(400, easing = androidx.compose.animation.core.LinearEasing)
            ), label = label
        )
        val modifier = Modifier.graphicsLayer {
            rotationZ = angle
        }
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        ) {
            PokeBallSmall(tint = colorResource(R.color.poke_light_red), modifier = modifier)
        }
    }
}

@Composable
private fun ErrorView(errorMessage: String) {
    Box {
        Column {
            Text(
                text = AnnotatedString(
                    text = errorMessage,
                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center)
                ), style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorResource(R.color.poke_red)
                ), modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun ContentView(
    pokemons: List<PokemonListItem>, onPokemonSelected: (PokemonListItem) -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Title(
                text = "ContentView",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    top = 64.dp, bottom = 24.dp
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), contentPadding = PaddingValues(4.dp)
            ) {
                items(pokemons) {
                    PokeDexCard(it, onPokemonSelected)
                }
            }
        }
    }
}

@Composable
fun PokeDexCard(
    pokemonListItem: PokemonListItem,
    onPokemonSelected: (PokemonListItem) -> Unit,
    viewModel: PokemonViewModel = viewModel()
) {
    val pokemon = viewModel.pokemonDetails.observeAsState()
    pokemon.value?.let {
        Surface(
            color = colorResource(it.color()), shape = RoundedCornerShape(16.dp)
        ) {
            PokeDexCardContent(it, pokemonListItem, onPokemonSelected)
        }
    }
}

@Composable
private fun PokeDexCardContent(
    pokemon: Pokemon, pokemonListItem: PokemonListItem, onPokemonSelected: (PokemonListItem) -> Unit
) {
    Box(modifier = Modifier
        .height(120.dp)
        .fillMaxWidth()
        .clickable { onPokemonSelected(pokemonListItem) }) {
        Column(modifier = Modifier.padding(top = 8.dp, start = 12.dp)) {
            PokemonName(pokemon.name)
            PokemonTypeLabels(pokemon.types.map { it.type?.name ?: "" }.toList(), SMALL)
        }

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.padding(top = 8.dp, end = 12.dp)
        ) {
            PokemonId(pokemon.id.toString())
        }

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .offset(x = 5.dp, y = 10.dp)
                .size(96.dp)
        ) {
            PokeBallSmall(
                Color.White, 0.25f
            )
        }

        pokemon.sprites?.other?.dreamWorld?.frontDefault?.let { imageUrl ->
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .size(72.dp)
            ) {
                LoadImageFromSvgUrl(imageUrl)
            }
        }
    }
}

@Composable
private fun PokemonName(text: String?) {
    Text(
        text = text ?: "MissingName", style = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(R.color.white_1000)
        ), modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun PokemonId(text: String?) {
    Text(
        text = text ?: "MissingNo", // oh no
        modifier = Modifier.alpha(0.1f), style = TextStyle(
            fontFamily = appFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp
        )
    )
}

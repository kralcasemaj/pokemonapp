package com.example.pokemonlist.ui.pokedex

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonlist.R
import com.example.pokemonlist.model.pokemon.Pokemon
import com.example.pokemonlist.model.pokemon.PokemonListItem
import com.example.pokemonlist.model.pokemon.color
import com.example.pokemonlist.ui.appFontFamily
import com.example.pokemonlist.ui.common.LoadImageFromSvgUrl
import com.example.pokemonlist.ui.common.PokeBallBackground
import com.example.pokemonlist.ui.common.PokeBallSmall
import com.example.pokemonlist.ui.common.PokemonTypeLabels
import com.example.pokemonlist.ui.common.SearchField
import com.example.pokemonlist.ui.common.TypeLabelMetrics.Companion.SMALL
import com.example.pokemonlist.ui.viewmodel.PokemonViewModel

interface PokemonList {

    companion object {
        @Composable
        fun Content(
            onPokemonSelected: (PokemonListItem) -> Unit, viewModel: PokemonViewModel = viewModel()
        ) {
            val viewModelState by viewModel.loadingState.collectAsState()
            val errorMessage by viewModel.errorMessage.collectAsState()

            viewModel.getPokemonList()

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxSize()
                ) {
                    PokeBallBackground()
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Crossfade(targetState = viewModelState, label = "pokemonList") {
                        when (it) {
                            PokemonViewModel.State.Initialised, PokemonViewModel.State.Loading -> LoadingView()
                            PokemonViewModel.State.Error -> ErrorView(errorMessage!!)
                            PokemonViewModel.State.Result -> ContentView(
                                onPokemonSelected
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
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
    onPokemonSelected: (PokemonListItem) -> Unit, viewModel: PokemonViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQueryText.collectAsState()
    val pokemonsSearched by viewModel.pokemons.collectAsState()

    pokemonsSearched.let { pokemons ->
        Column {
            SearchField(
                searchQuery = searchQuery,
                onQueryChanged = viewModel::onQueryTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp)
            )

            Box(modifier = Modifier.padding(4.dp)) {
                LazyVerticalGrid(
                    modifier = Modifier,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(32.dp),
                    content = {
                        items(pokemons.size) {
                            PokeDexCard(pokemons[it], onPokemonSelected)
                        }
                    }
                )
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
    val pokemonMap = viewModel.pokemonDetails.collectAsState()

    viewModel.getPokemonDetails(pokemonListItem)

    pokemonMap.value[pokemonListItem.name]?.let { pokemon ->
        Surface(
            modifier = Modifier.padding(8.dp),
            color = colorResource(pokemon.color()), shape = RoundedCornerShape(16.dp)
        ) {
            PokeDexCardContent(pokemon, pokemonListItem, onPokemonSelected)
        }
    }
}

@Composable
private fun PokeDexCardContent(
    pokemon: Pokemon, pokemonListItem: PokemonListItem, onPokemonSelected: (PokemonListItem) -> Unit
) {
    Box(modifier = Modifier
        .height(120.dp)
        .clickable { onPokemonSelected(pokemonListItem) }) {
        Column(modifier = Modifier.padding(top = 8.dp, start = 12.dp)) {
            PokemonName(pokemon.name?.capitalize(Locale.current))
            PokemonTypeLabels(pokemon.types.map { it.type?.name ?: "" }.toList(), SMALL)
        }

        Box(
            modifier = Modifier
                .padding(top = 8.dp, end = 12.dp)
                .align(Alignment.TopEnd)
        ) {
            PokemonId(pokemon.id.toString())
        }

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .offset(x = 5.dp, y = 10.dp)
                .size(96.dp)
                .align(Alignment.TopEnd)
        ) {
            PokeBallSmall(
                Color.White, 0.25f
            )
        }

        pokemon.sprites?.other?.dreamWorld?.frontDefault?.let { imageUrl ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(72.dp)
                    .padding(bottom = 8.dp, end = 8.dp)
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
        text = text ?: "MissingNo",
        style = TextStyle(
            color = colorResource(R.color.white_1000),
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    )
}

package com.example.pokemonlist

import android.os.Build
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.pokemonlist.api.PokemonAPI
import com.example.pokemonlist.model.pokemon.DreamWorld
import com.example.pokemonlist.model.pokemon.Other
import com.example.pokemonlist.model.pokemon.Pokemon
import com.example.pokemonlist.model.pokemon.PokemonListItem
import com.example.pokemonlist.model.pokemon.Sprites
import com.example.pokemonlist.model.pokemon.Type
import com.example.pokemonlist.model.pokemon.Types
import com.example.pokemonlist.ui.pokedex.CardContent
import com.example.pokemonlist.ui.pokedex.HeaderLeft
import com.example.pokemonlist.ui.pokedex.HeaderRight
import com.example.pokemonlist.ui.pokedex.PokemonDetails
import com.example.pokemonlist.ui.pokedex.PokemonImage
import com.example.pokemonlist.ui.pokedex.RotatingPokeBall
import com.example.pokemonlist.ui.viewmodel.PokemonViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE], application = PokemonApp::class)
class PokemonDetailsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: PokemonViewModel
    private val pokemonAPI = mockk<PokemonAPI>()
    private val mockPokemonListItem = PokemonListItem("Pikachu", "url")

    @Before
    fun setUp() {
        viewModel = PokemonViewModel(pokemonAPI)
        val mockDetailsResponse =
            Response.success(
                Pokemon(
                    id = 25,
                    name = "Pikachu",
                    types = arrayListOf(Types(1, Type("Electric"))),
                    sprites = Sprites(),
                )
            )
        coEvery { pokemonAPI.getPokemonDetails(any()) } returns mockDetailsResponse
    }

    @Test
    fun testRotatingPokeBall() {
        composeTestRule.setContent {
            RotatingPokeBall()
        }

        composeTestRule.onNodeWithTag("RotatingPokeBall").assertExists()
    }

    @Test
    fun testHeaderRight() {
        val pokemon = Pokemon(
            id = 25,
            name = "Pikachu",
            types = arrayListOf(Types(1, Type("Electric"))),
            sprites = Sprites()
        )

        composeTestRule.setContent {
            HeaderRight(pokemon = pokemon)
        }

        composeTestRule.onNodeWithText("25").assertExists()
    }

    @Test
    fun testHeaderLeft() {
        val pokemon = Pokemon(
            id = 25,
            name = "Pikachu",
            types = arrayListOf(Types(1, Type("Electric"))),
            sprites = Sprites()
        )

        composeTestRule.setContent {
            HeaderLeft(pokemon = pokemon)
        }

        composeTestRule.onNodeWithText("Pikachu").assertExists()
        composeTestRule.onNodeWithText("Electric").assertExists()
    }

    @Test
    fun testCardContent() {
        val pokemon = Pokemon(
            id = 25,
            name = "Pikachu",
            types = arrayListOf(Types(1, Type("Electric"))),
            sprites = Sprites()
        )

        composeTestRule.setContent {
            CardContent(pokemon = pokemon)
        }

        composeTestRule.onNodeWithText("Abilities").assertExists()
        composeTestRule.onNodeWithText("Base stats").assertExists()
        composeTestRule.onNodeWithText("Moves").assertExists()
        composeTestRule.onNodeWithText("Other").assertExists()
    }

    @Test
    fun testPokemonImage() {
        val pokemon = Pokemon(
            id = 25,
            name = "Pikachu",
            types = arrayListOf(Types(1, Type("Electric"))),
            sprites = Sprites(other = Other(dreamWorld = DreamWorld(frontDefault = "url")))
        )

        composeTestRule.setContent {
            PokemonImage(pokemon = pokemon)
        }

        // Assuming LoadImageFromSvgUrl is a composable function displaying the image.
        composeTestRule.onNodeWithContentDescription("Pikachu").assertExists()
    }

    @Test
    fun testContent() {
        composeTestRule.setContent {
            PokemonDetails.Content(pokemonListIem = mockPokemonListItem, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Pikachu").assertExists()
        composeTestRule.onNodeWithText("Electric").assertExists()
    }
}
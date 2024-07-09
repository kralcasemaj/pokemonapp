package com.example.pokemonlist

import android.os.Build
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.pokemonlist.api.PokemonAPI
import com.example.pokemonlist.model.pokemon.Pokemon
import com.example.pokemonlist.model.pokemon.PokemonListItem
import com.example.pokemonlist.model.pokemon.Sprites
import com.example.pokemonlist.model.pokemon.Type
import com.example.pokemonlist.model.pokemon.Types
import com.example.pokemonlist.ui.pokedex.ErrorView
import com.example.pokemonlist.ui.pokedex.LoadingView
import com.example.pokemonlist.ui.pokedex.PokeDexCard
import com.example.pokemonlist.ui.pokedex.PokemonId
import com.example.pokemonlist.ui.pokedex.PokemonList
import com.example.pokemonlist.ui.pokedex.PokemonName
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
class PokemonListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: PokemonViewModel
    private val pokemonAPI = mockk<PokemonAPI>()
    private val mockPokemonListItem = PokemonListItem("Pikachu", "url")

    @Before
    fun setUp() {
        viewModel = PokemonViewModel(pokemonAPI)
        val mockPokemonList =
            Response.success(
                com.example.pokemonlist.model.pokemon.PokemonList(
                    results = listOf(
                        mockPokemonListItem
                    )
                )
            )
        val mockPokemon =
            Response.success(
                Pokemon(
                    id = 25,
                    name = "Pikachu",
                    types = arrayListOf(Types(1, Type("Electric"))),
                    sprites = Sprites(),
                )

            )
        coEvery { pokemonAPI.getPokemonList() } returns mockPokemonList
        coEvery { pokemonAPI.getPokemonDetails(any()) } returns mockPokemon
    }

    @Test
    fun testLoadingView() {
        composeTestRule.setContent {
            LoadingView()
        }

        composeTestRule.onNodeWithTag("LoadingView").assertExists()
    }

    @Test
    fun testErrorView() {
        val errorMessage = "Error occurred"
        composeTestRule.setContent {
            ErrorView(errorMessage = errorMessage)
        }

        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun testContentView() {
        composeTestRule.setContent {
            PokemonList.Content(onPokemonSelected = {}, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Pikachu").assertExists()
    }

    @Test
    fun testPokeDexCard() {
        composeTestRule.setContent {
            PokeDexCard(
                pokemonListItem = mockPokemonListItem,
                onPokemonSelected = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Pikachu").assertExists()
        composeTestRule.onNodeWithText("Electric").assertExists()
    }

    @Test
    fun testPokemonName() {
        val name = "Pikachu"
        composeTestRule.setContent {
            PokemonName(text = name)
        }

        composeTestRule.onNodeWithText(name).assertExists()
    }

    @Test
    fun testPokemonId() {
        val id = "25"
        composeTestRule.setContent {
            PokemonId(text = id)
        }

        composeTestRule.onNodeWithText(id).assertExists()
    }
}

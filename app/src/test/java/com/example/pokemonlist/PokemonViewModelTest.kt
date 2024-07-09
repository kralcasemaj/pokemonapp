package com.example.pokemonlist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pokemonlist.api.PokemonAPI
import com.example.pokemonlist.model.pokemon.Pokemon
import com.example.pokemonlist.model.pokemon.PokemonList
import com.example.pokemonlist.model.pokemon.PokemonListItem
import com.example.pokemonlist.model.pokemon.Sprites
import com.example.pokemonlist.model.pokemon.Type
import com.example.pokemonlist.model.pokemon.Types
import com.example.pokemonlist.ui.viewmodel.PokemonViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PokemonViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: PokemonViewModel
    private val pokemonAPI = mockk<PokemonAPI>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PokemonViewModel(pokemonAPI)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetPokemonList_Success() = runTest {
        val mockResponse =
            Response.success(PokemonList(results = listOf(PokemonListItem("Pikachu", "url"))))
        coEvery { pokemonAPI.getPokemonList() } returns mockResponse

        viewModel.getPokemonList()

        assertEquals(PokemonViewModel.State.Result, viewModel.loadingState.value)
        assertEquals(1, viewModel.pokemonList.value.size)
        assertEquals("Pikachu", viewModel.pokemonList.value[0].name)
    }

    @Test
    fun testGetPokemonList_Error() = runTest {
        val mockResponse = Response.error<PokemonList>(404, ResponseBody.create(null, ""))
        coEvery { pokemonAPI.getPokemonList() } returns mockResponse

        viewModel.getPokemonList()

        assertEquals(PokemonViewModel.State.Error, viewModel.loadingState.value)
    }

    @Test
    fun testGetPokemonDetails_Success() = runTest {
        val mockPokemonListItem = PokemonListItem("Pikachu", "url")
        val mockDetailsResponse = Response.success(
            Pokemon(
                id = 25,
                name = "Pikachu",
                types = arrayListOf(Types(1, Type("Electric"))),
                sprites = Sprites(),
            )
        )
        coEvery { pokemonAPI.getPokemonDetails(any()) } returns mockDetailsResponse

        viewModel.getPokemonDetails(mockPokemonListItem)

        assertEquals(25, viewModel.pokemonDetails.value["Pikachu"]?.id)
        assertEquals("Pikachu", viewModel.pokemonDetails.value["Pikachu"]?.name)
        assertEquals(1, viewModel.pokemonDetails.value["Pikachu"]?.types?.size)
        assertEquals(
            "Electric",
            viewModel.pokemonDetails.value["Pikachu"]?.types?.firstOrNull()?.type?.name
        )
    }

    @Test
    fun testGetPokemonDetails_Error() = runTest {
        val mockPokemonListItem = PokemonListItem("Pikachu", "url")
        val mockError = Response.error<Pokemon>(404, ResponseBody.create(null, ""))
        coEvery { pokemonAPI.getPokemonDetails(any()) } returns mockError

        viewModel.getPokemonDetails(mockPokemonListItem)

        assertNull(viewModel.pokemonDetails.value["Pikachu"]) // Ensure details are not updated on error
        assertNotNull(viewModel.errorMessage.value) // Check if error message is set
        assertEquals(PokemonViewModel.State.Error, viewModel.loadingState.value)
    }
}

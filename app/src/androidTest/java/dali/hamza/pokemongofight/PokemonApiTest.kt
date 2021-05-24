package dali.hamza.pokemongofight

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class PokemonApiTest {

    private val api = Retrofit.Builder()
        .baseUrl( "https://pokeapi.co/api/v2/pokemon/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ClientApi::class.java)

    @Before
    fun init() {
    }

    @Test
    fun getPokemonListTest() = runBlocking {
        val response = api.getListPokemonFomPokeApi(
            "https://pokeapi.co/api/v2/pokemon/",
            10,
            1000,
        )
        print(response)
    }

}
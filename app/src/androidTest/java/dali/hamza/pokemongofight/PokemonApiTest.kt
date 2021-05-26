package dali.hamza.pokemongofight

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.core.datasource.network.PokeApiClient
import dali.hamza.core.repository.data
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.repository.IPokemonRepository
import dali.hamza.pokemongofight.test.R
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class PokemonApiTest {

    private lateinit var api: ClientApi

    private val pokeApi = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(PokeApiClient::class.java)

    @Before
    fun init() {
        val testContext: Context = getInstrumentation().context

        api = Retrofit.Builder()
            .baseUrl(testContext.resources.getString(R.string.serverUrl))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ClientApi::class.java)
    }

    @Test
    fun getPokemonListTest() = runBlocking {
        val response = pokeApi.getListPokemonFomPokeApi(
            10,
            1000,
        )
        assert(response.isSuccessful)
    }

    @Test
    fun getCommunityPokemonListTest() = runBlocking {
        val response = api.getCommunityListPokemon(
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJ6T0hHZUw0UVBsZTBNcVZpbEgybVRFbkF5Z0UyIiwiaWF0IjoxNjIxOTI4NTUwLCJleHAiOjE2MjE5MzIxNTB9.azOkcia7IgFNmuU1dZmzociOjPZH-PocHIKFma67jWw"
        )
        assert(response.isSuccessful)
    }

    @Test
    fun getDetailPokemonListTest() = runBlocking {
        val response = pokeApi.getDetailPokemonFomPokeApi(
            id = 1
        )
        val detailPoke = response.data {
            it.stats.filter { s->
                s.stat.name=="hp"
            }.first()
        }
        assert(detailPoke.data?.base_stat == 45)
    }

}
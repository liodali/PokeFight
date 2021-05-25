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
        .baseUrl("https://us-central1-samaritan-android-assignment.cloudfunctions.net/")
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

    @Test
    fun getCommunityPokemonListTest() = runBlocking {
        val response = api.getCommunityListPokemon(
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJ6T0hHZUw0UVBsZTBNcVZpbEgybVRFbkF5Z0UyIiwiaWF0IjoxNjIxOTI4NTUwLCJleHAiOjE2MjE5MzIxNTB9.azOkcia7IgFNmuU1dZmzociOjPZH-PocHIKFma67jWw"
        )
        print(response)
    }

}
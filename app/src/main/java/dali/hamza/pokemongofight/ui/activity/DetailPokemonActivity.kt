package dali.hamza.pokemongofight.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.domain.models.UserPokemon
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.ActivityDetailPokemonBinding

class DetailPokemonActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailPokemonBinding
    private lateinit var toolbar: MaterialToolbar


    private var pokemonGeoPoint: PokemonWithGeoPoint? = null
    private var pokemon: Pokemon? = null
    private var pokeGeoPoint: PokeGeoPoint? = null
    private var pokemonUser: UserPokemon? = null
    private var typeDetail: String? = null

    private var isCaptured: Boolean = false
    private var isWild: Boolean = false
    private var isOther: Boolean = false


    companion object {

        const val keyTypeDetail = "keyTypePoke"
        const val keyPoke = "keyPoke"
        const val keyMePoke = "keyMePoke"
        const val keyPokeUser = "keyPokeUser"

        fun openDetailPokemonActivity(packageContext: Context) {
            val intent = Intent(packageContext, DetailPokemonActivity::class.java)
            packageContext.startActivity(intent)
        }

        fun openDetailPokemonActivityWithArgs(
            packageContext: Context,
            vararg args: Pair<String, Any>
        ) {
            val intent = Intent(packageContext, DetailPokemonActivity::class.java)
            val bundle = bundleOf(
                *args
            )
            intent.putExtras(bundle)
            packageContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveDataIntent()
        toolbar = binding.idToolbarDetailPokemon
        setSupportActionBar(toolbar)
        title = pokemon?.name ?: ""

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon!!.id}.png")
            .into(binding.idImgFrontPokemon)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${pokemon!!.id}.png")
            .into(binding.idImgBackPokemon)

    }

    private fun retrieveDataIntent() {
        when {
            intent.extras!!.containsKey(keyPoke) -> {
                pokemonGeoPoint = intent.extras!!.get(keyPoke) as PokemonWithGeoPoint?
                pokemon = pokemonGeoPoint!!.pokemon
                pokeGeoPoint = pokemonGeoPoint!!.pokeGeoPoint
            }
            intent.extras!!.containsKey(keyPokeUser) -> {
                pokemonUser = intent.extras!!.get(keyPokeUser) as UserPokemon?
                pokemon = pokemonUser!!.pokemon

            }
            intent.extras!!.containsKey(keyMePoke) -> {
                pokemonGeoPoint = intent.extras!!.get(keyPokeUser) as PokemonWithGeoPoint?
                pokemon = pokemonGeoPoint!!.pokemon
                pokeGeoPoint = pokemonGeoPoint!!.pokeGeoPoint
            }
            intent.extras!!.containsKey(keyTypeDetail) -> {
                when (intent.extras!!.getString(keyTypeDetail)!!.lowercase()) {
                    "me" -> {
                        isCaptured = true
                        isWild = false
                        isOther = false
                    }
                    "wild" -> {
                        isCaptured = false
                        isWild = true
                        isOther = false
                    }
                    "friends", "foes" -> {
                        isCaptured = false
                        isWild = false
                        isOther = true
                    }
                    else -> {
                        finish()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
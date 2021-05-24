package dali.hamza.pokemongofight.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import dali.hamza.pokemongofight.R

class DetailPokemonActivity : AppCompatActivity() {
    companion object {

        const val keyTypeDetail = "keyTypePoke"
        const val keyPoke = "keyPoke"

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
        setContentView(R.layout.activity_detail_pokemon)
    }
}
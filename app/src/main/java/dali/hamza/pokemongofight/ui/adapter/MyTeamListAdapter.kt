package dali.hamza.pokemongofight.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.squareup.picasso.Picasso
import dali.hamza.core.utilities.DateManager
import dali.hamza.domain.models.Community
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.domain.models.UserPokemon
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.ItemListCommunityPokemonBinding
import dali.hamza.pokemongofight.databinding.ItemPokmenMyTeamBinding

class MyTeamListAdapter constructor(
    val action: MyTeamItemCallback
) : BaseAdapter<PokemonWithGeoPoint, MyTeamListAdapter.ItemViewHolder>() {

    /// viewHolder
    class ItemViewHolder(
        private val bindingView: ItemPokmenMyTeamBinding,
        val action: MyTeamItemCallback
    ) : BaseAdapter.BaseViewHolder<PokemonWithGeoPoint>(bindingView) {
        override fun bind(data: PokemonWithGeoPoint) {
            bindingView.idNamePokeMyTeam.text = data.pokemon.name
            bindingView.idTimeCapturedMyTeam.text = bindingView.root.context.resources.getString(
                R.string.captured_at
            ) + " " + data.pokemon.captured_at

            Picasso.get()
                .load("https://pokeres.bastionbot.org/images/pokemon/${data.pokemon.id}.png")
                .into(bindingView.idPokeImageMyTeam)

            bindingView.root.setOnClickListener {
                action.goToDetailPokemon(data,"Me")
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                action: MyTeamItemCallback
            ): ItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPokmenMyTeamBinding.inflate(inflater, parent, false)
                return ItemViewHolder(
                    binding,
                    action = action
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(
            parent = parent,
            action = action
        )
    }

    interface MyTeamItemCallback {
        fun goToDetailPokemon(
            pokemon: PokemonWithGeoPoint,
            type: String,
        )
    }
}
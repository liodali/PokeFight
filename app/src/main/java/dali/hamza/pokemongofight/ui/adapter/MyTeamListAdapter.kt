package dali.hamza.pokemongofight.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.ItemPokmenMyTeamBinding


class MyTeamListAdapter constructor(
   private  val action: MyTeamItemCallback
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
                action.goToDetailPokemonWithHeroAnimation(
                    data,
                    "Me",
                    bindingView.idPokeImageMyTeam
                )
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
        fun goToDetailPokemonWithHeroAnimation(
            pokemon: PokemonWithGeoPoint,
            type: String,
            sourceAnimationHero: View
        )
    }
}
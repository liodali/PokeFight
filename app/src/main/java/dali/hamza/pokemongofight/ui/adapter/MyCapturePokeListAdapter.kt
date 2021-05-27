package dali.hamza.pokemongofight.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.ItemCapturedPokeBinding
import dali.hamza.pokemongofight.databinding.ItemPokmenMyTeamBinding


class MyCapturePokeListAdapter constructor(
   private  val action: MyCapturedItemCallback
) : BaseAdapter<PokemonWithGeoPoint, MyCapturePokeListAdapter.ItemViewHolder>() {

    /// viewHolder
    class ItemViewHolder(
        private val bindingView: ItemCapturedPokeBinding,
        val action: MyCapturedItemCallback
    ) : BaseAdapter.BaseViewHolder<PokemonWithGeoPoint>(bindingView) {
        override fun bind(data: PokemonWithGeoPoint) {

            Picasso.get()
                .load("https://pokeres.bastionbot.org/images/pokemon/${data.pokemon.id}.png")
                .into(bindingView.idPokeCapturedItem)

            bindingView.root.setOnClickListener {
                action.goToDetailPokemonWithHeroAnimation(
                    data,
                    "Me",
                    bindingView.idPokeCapturedItem
                )
            }
        }
        companion object {
            fun create(
                parent: ViewGroup,
                action: MyCapturedItemCallback
            ): ItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCapturedPokeBinding.inflate(inflater, parent, false)
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

    interface MyCapturedItemCallback {
        fun goToDetailPokemonWithHeroAnimation(
            pokemon: PokemonWithGeoPoint,
            type: String,
            sourceAnimationHero: View
        )
    }
}
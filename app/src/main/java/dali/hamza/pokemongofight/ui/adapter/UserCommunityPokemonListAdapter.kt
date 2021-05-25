package dali.hamza.pokemongofight.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.squareup.picasso.Picasso
import dali.hamza.domain.models.Community
import dali.hamza.domain.models.UserPokemon
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.showDateDiffMessage
import dali.hamza.pokemongofight.databinding.ItemListCommunityPokemonBinding
import dali.hamza.pokemongofight.databinding.SubItemUserPokeCommunityBinding

class UserCommunityPokemonListAdapter  : BaseAdapter<UserPokemon, UserCommunityPokemonListAdapter.ItemViewHolder>() {

    /// viewHolder
    class ItemViewHolder(
        private val bindingView: SubItemUserPokeCommunityBinding,
    ) : BaseAdapter.BaseViewHolder<UserPokemon>(bindingView) {
        override fun bind(data: UserPokemon) {
            //CommunityListAdapter
            bindingView.idTextNameTrainer.text = data.name
            bindingView.idCapturedStatus.text = "Captured"
            bindingView.idTextNameTime.text =
                "${data.pokemon.name} ${showDateDiffMessage(dateCreatedAt = data.pokemon.captured_at!!)}"

            Picasso.get()
                .load("https://pokeres.bastionbot.org/images/pokemon/${data.pokemon.id}.png")
                .into(bindingView.idImagePokemon)

        }


        companion object {
            fun create(parent: ViewGroup): ItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = SubItemUserPokeCommunityBinding.inflate(inflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent = parent)
    }

}
package dali.hamza.pokemongofight.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dali.hamza.domain.models.Community
import dali.hamza.pokemongofight.databinding.ItemListCommunityPokemonBinding

class CommunityListAdapter  : BaseAdapter<Community, CommunityListAdapter.ItemViewHolder>() {

    /// viewHolder
    class ItemViewHolder(
        private val bindingView: ItemListCommunityPokemonBinding,
    ) : BaseAdapter.BaseViewHolder<Community>(bindingView) {
        override fun bind(data: Community) {
            bindingView.idTitleGroupCommunity.text = data.name.replaceFirst(data.name[0], data.name[0].uppercaseChar())
            val adapter = UserCommunityPokemonListAdapter()
            bindingView.idRecyclerUserCommunityList.layoutManager = LinearLayoutManager(bindingView.root.context,LinearLayoutManager.HORIZONTAL,false)
            bindingView.idRecyclerUserCommunityList.adapter = adapter
            adapter.addListWithClear(data = data.listUserPokemon)
        }

        
        companion object {
            fun create(parent: ViewGroup): ItemViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemListCommunityPokemonBinding.inflate(inflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent = parent)
    }

}
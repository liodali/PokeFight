package dali.hamza.pokemongofight.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, E : BaseAdapter.BaseViewHolder<T>>(
    internal val list: MutableList<T> = emptyList<T>().toMutableList()
) :
    RecyclerView.Adapter<E>() {

    fun addList(data: List<T>) {
        val len = when (list.isEmpty()) {
            true -> 0
            else -> list.size
        }

        list.addAll(data)
        notifyItemRangeInserted(len, data.size)
    }

    fun addListWithClear(data: List<T>) {
        clear()
        list.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun clear() {
        val len = list.size
        list.clear()
        notifyItemRangeRemoved(0, len)
    }

    abstract class BaseViewHolder<T>(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(data: T)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: E, position: Int) {
        val data: T = list[position]
        holder.bind(data)
    }

    fun removeItemPosition(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

}
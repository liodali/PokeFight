package dali.hamza.pokemongofight.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class MainPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val listFrags: MutableSet<Fragment> = emptyList<Fragment>().toMutableSet()

    init {
        listFrags.clear()
        listFrags.addAll(fragmentActivity.supportFragmentManager.fragments)

    }

    constructor(
        fragmentActivity: FragmentActivity,
        fragmentPager: List<Fragment>
    ) : this(fragmentActivity) {
        listFrags.addAll(fragmentPager)
    }

    fun initFrag(fragmentPager: List<Fragment>) {
        listFrags.addAll(fragmentPager)
    }

    override fun getItemCount(): Int {
        return listFrags.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFrags.elementAt(position)
    }


}
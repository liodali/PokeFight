package dali.hamza.pokemongofight.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.ActivityMainBinding
import dali.hamza.pokemongofight.ui.adapter.MainPagerAdapter
import dali.hamza.pokemongofight.ui.fragment.CapturedFragment
import dali.hamza.pokemongofight.ui.fragment.CommunityFragment
import dali.hamza.pokemongofight.ui.fragment.ExploreFragment
import dali.hamza.pokemongofight.ui.fragment.MyTeamFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /// init UI ELEMENT
    private lateinit var binding: ActivityMainBinding
    private lateinit var rootPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    ///init fragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var myTeamFragment: MyTeamFragment
    private lateinit var capturedFragment: CapturedFragment

    private var selectedIndex: Int = 0

    private val arrayNamesTabItem by lazy {
        resources.getStringArray(R.array.tabs_names)
    }
    private val adapterPager by lazy {
        MainPagerAdapter(this)
    }

    private val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            selectedIndex = position
            when (selectedIndex) {
                0 -> rootPager.isUserInputEnabled = false
                else -> rootPager.isUserInputEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPageManageFrags(savedInstanceState = savedInstanceState)

        setupUI()



        TabLayoutMediator(tabLayout, rootPager) { tab, position ->
            tab.setCustomView(R.layout.poke_tab_item).also {
                val textView = it.customView?.findViewById<TextView>(R.id.id_tab_item_label)
                textView?.text = arrayNamesTabItem[position]
            }
        }.attach()
        rootPager.registerOnPageChangeCallback(pagerCallback)

        rootPager.currentItem = selectedIndex


    }

    override fun onDestroy() {
        super.onDestroy()
        rootPager.unregisterOnPageChangeCallback(pagerCallback)

    }


    override fun onBackPressed() {
        if (rootPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            rootPager.currentItem = selectedIndex - 1
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedIndex", selectedIndex)
        if (supportFragmentManager.fragments
                .contains(exploreFragment)
        )

            supportFragmentManager
                .putFragment(
                    Bundle(),
                    ExploreFragment.key,
                    exploreFragment
                )
        if (supportFragmentManager.fragments.contains(communityFragment))
            supportFragmentManager.putFragment(
                Bundle(),
                CommunityFragment.key,
                communityFragment
            )
        if (supportFragmentManager.fragments.contains(myTeamFragment))
            supportFragmentManager.putFragment(
                Bundle(),
                MyTeamFragment.key,
                myTeamFragment
            )
        if (supportFragmentManager.fragments.contains(capturedFragment))
            supportFragmentManager.putFragment(
                Bundle(),
                CapturedFragment.key,
                capturedFragment
            )
    }

    private fun setupUI() {
        rootPager = binding.mainViewPager
        tabLayout = binding.idmMainTabLayout
        rootPager.adapter = adapterPager

        rootPager.offscreenPageLimit = 4
    }

    private fun viewPageManageFrags(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            exploreFragment = ExploreFragment.newInstance()
            communityFragment = CommunityFragment.newInstance()
            myTeamFragment = MyTeamFragment.newInstance()
            capturedFragment = CapturedFragment.newInstance()


            adapterPager.initFrag(
                listOf(
                    exploreFragment,
                    communityFragment,
                    myTeamFragment,
                    capturedFragment,
                )
            )


        } else {
            selectedIndex = savedInstanceState.getInt("selectedIndex", 0)
            when (selectedIndex) {
                0 -> rootPager.isUserInputEnabled = false
                else -> rootPager.isUserInputEnabled = true
            }
            exploreFragment = ExploreFragment.newInstance()
            communityFragment = supportFragmentManager.fragments[1] as CommunityFragment
            myTeamFragment = supportFragmentManager.fragments[2] as MyTeamFragment
            capturedFragment = supportFragmentManager.fragments.last() as CapturedFragment

            adapterPager.initFrag(
                supportFragmentManager.fragments
            )
        }
    }

}
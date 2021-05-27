package dali.hamza.pokemongofight.ui.fragment

import android.app.ActivityOptions
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.domain.models.*
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.EmptyBinding
import dali.hamza.pokemongofight.databinding.FragmentMyTeamBinding
import dali.hamza.pokemongofight.ui.activity.DetailPokemonActivity
import dali.hamza.pokemongofight.ui.adapter.MyTeamListAdapter
import dali.hamza.pokemongofight.viewmodels.MyTeamViewModel
import kotlinx.coroutines.flow.collect


/**
 * A simple [Fragment] subclass.
 * Use the [MyTeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyTeamFragment : Fragment(), MyTeamListAdapter.MyTeamItemCallback {


    private lateinit var binding: FragmentMyTeamBinding
    private lateinit var swipeRefreshList: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: View
    private lateinit var emptyView: EmptyBinding


    private val myTeamListAdapter: MyTeamListAdapter by lazy {
        MyTeamListAdapter(this)
    }


    private val viewModel: MyTeamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyTeamBinding.inflate(inflater, container, false)
        recyclerView = binding.idRecyclerMyTeamList
        swipeRefreshList = binding.idSwipeRefreshListMyTeam
        emptyView = binding.idEmptyListMyteam
        loadingView = binding.idLoadingMyTeam.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = myTeamListAdapter
        recyclerView.layoutManager =
            GridLayoutManager(
                requireContext(),
                1,
                GridLayoutManager.VERTICAL,
                false
            )

        lifecycleScope.launchWhenStarted {
            viewModel.getFlowMyTeamPokemon().collect { response ->
                if (response != null) {
                    if (swipeRefreshList.isRefreshing) {
                        swipeRefreshList.isRefreshing = false
                    }
                    buildUI(response)
                    if (!swipeRefreshList.isVisible && loadingView.isVisible) {
                        swipeRefreshList.visible()
                        loadingView.gone()
                    }

                }
            }
        }
        swipeRefreshList.setOnRefreshListener {
            viewModel.fetchForMyTeamPokemon()
        }
        viewModel.fetchForMyTeamPokemon()
    }

    private fun buildUI(response: IResponse) {
        when (response) {
            is MyResponse.SuccessResponse<*> -> {
                val listMyTeam = response.data as List<PokemonWithGeoPoint>
                myTeamUI(listMyTeam)
            }
            else -> {
                showEmptyListMyTeam()
            }
        }
    }

    private fun myTeamUI(listMyTeam: List<PokemonWithGeoPoint>) {
        when (listMyTeam.isEmpty()) {
            false -> {
                myTeamListAdapter.addListWithClear(listMyTeam)
                recyclerView.visible()
                emptyView.root.gone()
            }
            else -> {
                showEmptyListMyTeam()
            }
        }
    }

    private fun showEmptyListMyTeam() {
        recyclerView.gone()
        emptyView.idEmptyList.visible()
    }

    companion object {
        const val key = "myTeamFrag"

        @JvmStatic
        fun newInstance() =
            MyTeamFragment().apply {
                arguments = Bundle()
            }
    }

    override fun goToDetailPokemonWithHeroAnimation(
        pokemon: PokemonWithGeoPoint,
        type: String,
        sourceAnimationHero: View
    ) {
        val options = ActivityOptions
            .makeSceneTransitionAnimation(
                requireActivity(),
                sourceAnimationHero,
                resources.getString(R.string.transition_hero_item_destination_name)
            )
        DetailPokemonActivity.openDetailPokemonActivityWithArgsAndHeroAnimation(
            requireContext(),
            options,
            DetailPokemonActivity.keyMePoke to pokemon,
            DetailPokemonActivity.keyTypeDetail to type
        )
    }
}
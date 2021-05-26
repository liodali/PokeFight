package dali.hamza.pokemongofight.ui.fragment

import android.app.ActivityOptions
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.domain.models.Community
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.UserPokemon
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.FragmentCommunityBinding
import dali.hamza.pokemongofight.ui.activity.DetailPokemonActivity
import dali.hamza.pokemongofight.ui.adapter.CommunityListAdapter
import dali.hamza.pokemongofight.ui.adapter.UserCommunityPokemonListAdapter
import dali.hamza.pokemongofight.viewmodels.CommunityViewModel
import kotlinx.coroutines.flow.collect


/**
 * A simple [Fragment] subclass.
 * Use the [CommunityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CommunityFragment : Fragment(),
    UserCommunityPokemonListAdapter.UserCommunityCallback {

    private lateinit var binding: FragmentCommunityBinding

    private val viewModel: CommunityViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadingView: View

    private val adapter: CommunityListAdapter by lazy {
        CommunityListAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        recyclerView = binding.idRecyclerCommunityList
        swipeRefreshLayout = binding.idSwipeRefreshListCommunity
        loadingView = binding.idLoadingCommunity.idLoadingLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.getFlowCommunityPokemon().collect { response ->
                if (response != null) {
                    when (response) {
                        is MyResponse.SuccessResponse<*> -> {
                            val communities = response.data as List<Community>
                            adapter.addListWithClear(communities)
                            swipeRefreshLayout.visible()
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                        else -> {

                        }
                    }
                    loadingView.gone()
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            //refreshUI()
            viewModel.fetchForCommunityPokemon()
        }

        viewModel.fetchForCommunityPokemon()

    }

    override fun onStart() {
        super.onStart()

    }


    private fun refreshUI() {
        adapter.clear()
        loadingView.visible()
        swipeRefreshLayout.gone()

    }

    companion object {
        const val key = "communityFrag"

        @JvmStatic
        fun newInstance() =
            CommunityFragment().apply {
                arguments = Bundle()
            }
    }

    override fun goToDetailPokemonWithHeroAnimation(
        pokemon: UserPokemon,
        type: String,
        sourceAnimationView: View
    ) {
        val options = ActivityOptions
            .makeSceneTransitionAnimation(
                requireActivity(),
                sourceAnimationView,
                resources.getString(R.string.transition_hero_item_destination_name)
            )
        DetailPokemonActivity.openDetailPokemonActivityWithArgsAndHeroAnimation(
            requireActivity(),
            options,
            DetailPokemonActivity.keyPokeUser to pokemon,
            DetailPokemonActivity.keyTypeDetail to type
        )
    }
}
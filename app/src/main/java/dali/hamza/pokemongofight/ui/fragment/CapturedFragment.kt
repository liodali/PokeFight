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
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.onData
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.EmptyBinding
import dali.hamza.pokemongofight.databinding.FragmentCapturedBinding
import dali.hamza.pokemongofight.ui.activity.DetailPokemonActivity
import dali.hamza.pokemongofight.ui.adapter.MyCapturePokeListAdapter
import dali.hamza.pokemongofight.viewmodels.CapturedViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [CapturedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CapturedFragment : Fragment(), MyCapturePokeListAdapter.MyCapturedItemCallback {


    private lateinit var binding: FragmentCapturedBinding
    private lateinit var swipeRefreshList: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: View
    private lateinit var emptyView: EmptyBinding


    private val viewModel: CapturedViewModel by viewModels()


    private val myCapturedAdapter: MyCapturePokeListAdapter by lazy {
        MyCapturePokeListAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCapturedBinding.inflate(inflater, container, false)
        recyclerView = binding.idRecyclerCapturedList
        swipeRefreshList = binding.idSwipeRefreshListCaptured
        emptyView = binding.idEmptyListCaptured
        loadingView = binding.idLoadingCaptured.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = myCapturedAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        swipeRefreshList.setOnRefreshListener {
            viewModel.fetchForCapturedPokemon()
        }

        viewModel.fetchForCapturedPokemon()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.getFlowCapturedPokemon()

                .onData(
                    error = {

                    }
                ) { response ->
                    if (swipeRefreshList.isRefreshing) {
                        swipeRefreshList.isRefreshing = false
                    }
                    val list =
                        (response as MyResponse.SuccessResponse<*>).data as List<PokemonWithGeoPoint>
                    when (list.isEmpty()) {
                        false -> {
                            myCapturedAdapter.addListWithClear(list)
                            if (loadingView.isVisible) {
                                showData()
                            }
                        }
                        else -> showEmpty()
                    }
                }
//                .collect { response->
//                    if(response != null ){
//                        val list = (response as MyResponse.SuccessResponse<*>).data as List<PokemonWithGeoPoint>
//                        when (list.isEmpty()) {
//                            false -> {
//                                myCapturedAdapter.addListWithClear(list)
//                                if (loadingView.isVisible) {
//                                    showData()
//                                }
//                            }
//                            else ->showEmpty()
//                        }
//                    }
//                }
        }
    }

    private fun showData() {
        loadingView.gone()
        swipeRefreshList.visible()
        emptyView.root.gone()
        recyclerView.visible()
    }

    private fun showEmpty() {
        loadingView.gone()
        swipeRefreshList.visible()
        emptyView.root.visible()
        recyclerView.gone()
    }

    companion object {
        const val key = "capturedFrag"


        @JvmStatic
        fun newInstance() =
            CapturedFragment().apply {
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
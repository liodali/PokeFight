package dali.hamza.pokemongofight.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.domain.models.Community
import dali.hamza.domain.models.MyResponse
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.FragmentCommunityBinding
import dali.hamza.pokemongofight.ui.adapter.CommunityListAdapter
import dali.hamza.pokemongofight.viewmodels.CommunityViewModel
import kotlinx.coroutines.flow.collect


/**
 * A simple [Fragment] subclass.
 * Use the [CommunityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CommunityFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding

    private val viewModel: CommunityViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: View

    private val adapter: CommunityListAdapter by lazy {
        CommunityListAdapter()
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
                            adapter.addList(communities)
                            recyclerView.visible()
                        }
                        else -> {

                        }
                    }
                    loadingView.gone()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchForCommunityPokemon()
    }

    override fun onPause() {
        super.onPause()
        refreshUI()
    }

    private fun refreshUI() {
        adapter.clear()
        loadingView.visible()
        recyclerView.gone()

    }

    companion object {
        const val key = "communityFrag"

        @JvmStatic
        fun newInstance() =
            CommunityFragment().apply {
                arguments = Bundle()
            }
    }
}
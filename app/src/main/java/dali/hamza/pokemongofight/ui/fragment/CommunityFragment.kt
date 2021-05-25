package dali.hamza.pokemongofight.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.databinding.FragmentCommunityBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.getFlowCommunityPokemon().collect { response ->

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchForCommunityPokemon()

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
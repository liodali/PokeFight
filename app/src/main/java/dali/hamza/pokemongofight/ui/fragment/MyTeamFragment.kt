package dali.hamza.pokemongofight.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.pokemongofight.R


/**
 * A simple [Fragment] subclass.
 * Use the [MyTeamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyTeamFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_team, container, false)
    }

    companion object {
        const val key = "myTeamFrag"

        @JvmStatic
        fun newInstance() =
            MyTeamFragment().apply {
                arguments = Bundle()
            }
    }
}
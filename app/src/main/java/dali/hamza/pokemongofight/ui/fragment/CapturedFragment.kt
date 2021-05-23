package dali.hamza.pokemongofight.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dali.hamza.pokemongofight.R



/**
 * A simple [Fragment] subclass.
 * Use the [CapturedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CapturedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_captured, container, false)
    }

    companion object {
        const val key = "capturedFrag"


        @JvmStatic
        fun newInstance() =
            CapturedFragment().apply {
                arguments = Bundle()
            }
    }
}
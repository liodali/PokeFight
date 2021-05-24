package dali.hamza.pokemongofight.ui.fragment

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.pokemongofight.databinding.FragmentExploreBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

import org.osmdroid.config.DefaultConfigurationProvider
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler

import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding


    private lateinit var map: MapView
    private lateinit var locationNewOverlay: MyLocationNewOverlay


    /**
     * request permission for gps and localisation
     */

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    locationNewOverlay.enableMyLocation()
                    locationNewOverlay.runOnFirstFix {
                        val geoP = locationNewOverlay.myLocation
                        lifecycleScope.launch(Main) {
                            map.controller.animateTo(geoP)
                            map.controller.setZoom(15.0)

                        }
                        // locationNewOverlay.disableMyLocation()
                    }
                    locationNewOverlay.enableFollowLocation()

                }
                else -> {
                    //TODO show dialog
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DefaultConfigurationProvider().getOsmdroidTileCache(requireContext())
        val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        map = binding.mapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.isTilesScaledToDpi = true
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationNewOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        permission.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    }

    override fun onResume() {
        super.onResume()
        val tileProvider =
            MapTileProviderBasic(requireContext().applicationContext, TileSourceFactory.MAPNIK)
        val mTileRequestCompleteHandler = SimpleInvalidationHandler(map)
        tileProvider.setTileRequestCompleteHandler(mTileRequestCompleteHandler)
        map.tileProvider = tileProvider
        map.onResume()
        if (locationNewOverlay.isFollowLocationEnabled) {
            locationNewOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
            locationNewOverlay.enableMyLocation()
            locationNewOverlay.enableFollowLocation()
        }
        map.overlays.add(locationNewOverlay)

    }


    override fun onPause() {
        super.onPause()
        map.overlays.remove(locationNewOverlay)
        map.onPause()

    }

    override fun onStop() {
        super.onStop()
        if (locationNewOverlay.isFollowLocationEnabled) {
            locationNewOverlay.disableFollowLocation()
        }
        if (locationNewOverlay.isMyLocationEnabled) {
            locationNewOverlay.disableMyLocation()
        }
    }

    override fun onDetach() {
        super.onDetach()
        map.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.tileProvider = null
    }

    private fun createPokemonPosition(){

    }




    companion object {
        const val key = "exploreFrag"

        @JvmStatic
        fun newInstance() =
            ExploreFragment().apply {
                arguments = Bundle()
            }
    }
}
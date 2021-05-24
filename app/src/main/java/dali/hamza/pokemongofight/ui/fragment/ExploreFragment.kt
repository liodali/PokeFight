package dali.hamza.pokemongofight.ui.fragment

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.toGeoPoint
import dali.hamza.pokemongofight.databinding.FragmentExploreBinding
import dali.hamza.pokemongofight.viewmodels.ExploreViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

import org.osmdroid.config.DefaultConfigurationProvider
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler

import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
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
    private var pokemonFolderMarkers: FolderOverlay = FolderOverlay().also {
        it.name = "pokmensPosition"
    }
    private val viewModel: ExploreViewModel by viewModels()
    private var gpsProvider: GpsMyLocationProvider? = null

    /**
     * request permission for gps and localisation
     */

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> {
                    locationNewOverlay.runOnFirstFix {
                        val geoP = locationNewOverlay.myLocation
                        lifecycleScope.launch(Main) {
                            map.controller.animateTo(geoP)
                            map.controller.setZoom(15.0)
                            withContext(IO) {
                                viewModel.exploreListPokemon(geoP)
                            }

                        }
                        // locationNewOverlay.disableMyLocation()
                    }
                    //locationNewOverlay.enableFollowLocation()
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
        //binding = FragmentExploreBinding.inflate(inflater, container, false)
        map = MapView(requireActivity())
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.isTilesScaledToDpi = true
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        return map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map.overlays.add(pokemonFolderMarkers)
        permission.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        lifecycleScope.launchWhenStarted {
            viewModel.getListPokemon().collect { response ->
                if (response != null) {
                    pokemonListDataManipulation(response = response)
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        if(!map.isAttachedToWindow){
            map.onAttachedToWindow()
        }
        val tileProvider =
            MapTileProviderBasic(requireContext().applicationContext, TileSourceFactory.MAPNIK)
        val mTileRequestCompleteHandler = SimpleInvalidationHandler(map)
        tileProvider.setTileRequestCompleteHandler(mTileRequestCompleteHandler)
        map.tileProvider = tileProvider
        map.onResume()
        gpsProvider = GpsMyLocationProvider(requireActivity())
        locationNewOverlay = MyLocationNewOverlay(gpsProvider, map)
        map.overlays.add(locationNewOverlay)
        locationNewOverlay.onResume()
        if (!map.overlays.contains(pokemonFolderMarkers)) {
            pokemonFolderMarkers = FolderOverlay().also {
                it.name = "pokmensPosition"
            }
            permission.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            map.overlays.add(pokemonFolderMarkers)
        }
    }


    override fun onPause() {
        locationNewOverlay.disableMyLocation()
        locationNewOverlay.onPause()
        map.onPause()
        super.onPause()
        //locationNewOverlay.disableMyLocation()

    }

    override fun onDetach() {
        map.onDetach()
        super.onDetach()
    }


    override fun onDestroy() {
        super.onDestroy()
        map.tileProvider = null
    }

    private suspend fun pokemonListDataManipulation(response: IResponse) {
        when (response) {
            is MyResponse.SuccessResponse<*> -> {
                val pokemons: List<PokemonWithGeoPoint> =
                    response.data as List<PokemonWithGeoPoint>
                withContext(IO) {
                    pokemons.forEach { pokemon ->
                        pokemonFolderMarkers.add(
                            Marker(map).also {
                                it.position = pokemon.pokeGeoPoint.toGeoPoint()
                                it.setInfoWindow(null)
                                it.icon = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_location_pokemon,
                                    requireContext().theme
                                )
                                it.setOnMarkerClickListener { marker, mapView ->
                                    true
                                }
                            }
                        )
                    }
                }
                if (map.overlays.contains(pokemonFolderMarkers)) {
                    map.overlays.remove(pokemonFolderMarkers)
                    map.overlays.add(pokemonFolderMarkers)
                    map.invalidate()
                }
            }
            else -> {

            }
        }
    }


    companion object {
        const val key = "exploreFrag"

        fun newInstance() =
            ExploreFragment().apply {
                arguments = Bundle()
            }
    }
}
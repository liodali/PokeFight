package dali.hamza.pokemongofight.ui.activity

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dali.hamza.core.utilities.DateManager
import dali.hamza.domain.models.*
import dali.hamza.pokemongofight.R
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.toGeoPoint
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.ActivityDetailPokemonBinding
import dali.hamza.pokemongofight.databinding.AddNewPokemonDialogLoadingBinding
import dali.hamza.pokemongofight.ui.fragment.CapturePokeDialog
import dali.hamza.pokemongofight.viewmodels.DetailPokemonViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*

@AndroidEntryPoint
class DetailPokemonActivity : AppCompatActivity(), CapturePokeDialog.CapturePokeCallback {


    private lateinit var binding: ActivityDetailPokemonBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var groupChipTypes: ChipGroup
    private lateinit var groupChipMoves: ChipGroup
    private lateinit var moreInformation: TextView
    private lateinit var nameUserCommunity: TextView
    private lateinit var pokeBall: FloatingActionButton
    private lateinit var cardViewLocation: MaterialCardView
    private lateinit var cardViewUserInformation: MaterialCardView
    private lateinit var captureButton: MaterialButton

    private lateinit var mapView: MapView


    private var pokemonGeoPoint: PokemonWithGeoPoint? = null
    private var pokemon: Pokemon? = null
    private var pokeGeoPoint: PokeGeoPoint? = null
    private var pokemonUser: UserPokemon? = null
    private var typeDetail: String? = null

    private var isCaptured: Boolean = false
    private var isWild: Boolean = false
    private var isOther: Boolean = false


    private val viewModel: DetailPokemonViewModel by viewModels()

    private var progress_dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveDataIntent()

        setupUI()




        viewModel.retrieveDetailPoke(pokemon!!.id)

    }

    private fun setupUI() {
        groupChipTypes = binding.idGroupChipTypePokemon
        groupChipMoves = binding.idGroupChipMovesPokemon
        moreInformation = binding.idCaptuedPokmenLabel
        nameUserCommunity = binding.idNameUserInfo



        pokeBall = binding.idCapturedPokemonFlBt
        toolbar = binding.idToolbarDetailPokemon

        captureButton = binding.idBtCapturePokemon
        pokeBall = binding.idCapturedPokemonFlBt

        mapView = binding.idDetailMapPokemonFound

        cardViewLocation = binding.idCardLocationPokemon
        cardViewLocation.apply {
            when (isOther) {
                true -> this.gone()
                false -> {

                    this.visible()
                    setupLocationPokeInMap(pokeGeoPoint!!)
                }
            }
        }


        cardViewUserInformation = binding.idUserCommunityInfo
        cardViewUserInformation.apply {
            when (isOther) {
                true -> {
                    this.visible()
                    nameUserCommunity.text =
                        "Capture by " + pokemonUser!!.typeCommunity + " " + pokemonUser!!.name
                }
                false -> this.gone()
            }
        }


        setSupportActionBar(toolbar)
        title = pokemon?.name ?: ""

        if (isCaptured) {
            pokeBall.visible()
        }

        val moreInfoTxt = when (pokemon!!.captured_at.isNotEmpty()) {
            true -> showDateWithAppFormat()
            else -> "-"
        }

        moreInformation.text =
            resources.getString(R.string.captured_at_detail, moreInfoTxt)


        if (isWild) {
            captureButton.visible()
            captureButton.setOnClickListener {
                val dialog = CapturePokeDialog.newInstance(
                    pokemon = PokemonWithGeoPoint(
                        pokemon = pokemon!!,
                        pokeGeoPoint = pokeGeoPoint!!
                    ),
                    this
                )
                dialog.show(supportFragmentManager.also {
                    val prevFrag = it.findFragmentByTag(CapturePokeDialog.tagChangePwdDialog)
                    if (prevFrag != null) {
                        it.beginTransaction().remove(prevFrag)
                    }
                    it.beginTransaction().addToBackStack(null)
                }, CapturePokeDialog.tagChangePwdDialog)
            }
        }

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon!!.id}.png")
            .into(binding.idImgFrontPokemon)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${pokemon!!.id}.png")
            .into(binding.idImgBackPokemon)
    }

    private fun buildTypePokemonUI(list: List<String>) {
        list.forEach { t ->
            groupChipTypes.addView(
                Chip(this).apply {
                    id = ViewCompat.generateViewId()
                    this.text = t
                    this.isCheckable = false
                }
            )
        }
        groupChipTypes.invalidate()
    }

    private fun buildMovesPokemonUI(list: List<String>) {
        list.forEach { t ->
            groupChipMoves.addView(
                Chip(this).apply {
                    id = ViewCompat.generateViewId()
                    this.text = t
                    this.isCheckable = false
                }
            )
        }
        groupChipMoves.invalidate()
    }

    private fun setupLocationPokeInMap(pokeGeoPoint: PokeGeoPoint) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.isTilesScaledToDpi = true
        mapView.setMultiTouchControls(true)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.controller.animateTo(pokeGeoPoint.toGeoPoint())
        mapView.controller.setZoom(15.0)
        mapView.overlays.add(
            Marker(mapView).also {
                it.position = pokeGeoPoint.toGeoPoint()
                it.setInfoWindow(null)
                it.icon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_location_pokemon,
                    this.theme
                )
            }
        )

    }

    private fun showDateWithAppFormat(): String {
        if (isOther)
            return DateManager.dateFormat_full.format(
                Date(
                    DateManager.convertStringFromFormatApiToApp(
                        pokemon!!.captured_at
                    )
                )
            )
        return pokemon!!.captured_at
    }

    private fun retrieveDataIntent() {
        when {
            intent.extras!!.containsKey(keyPoke) -> {
                pokemonGeoPoint = intent.extras!!.get(keyPoke) as PokemonWithGeoPoint?
                pokemon = pokemonGeoPoint!!.pokemon
                pokeGeoPoint = pokemonGeoPoint!!.pokeGeoPoint
            }
            intent.extras!!.containsKey(keyPokeUser) -> {
                pokemonUser = intent.extras!!.get(keyPokeUser) as UserPokemon?
                pokemon = pokemonUser!!.pokemon

            }
            intent.extras!!.containsKey(keyMePoke) -> {
                pokemonGeoPoint = intent.extras!!.get(keyMePoke) as PokemonWithGeoPoint?
                pokemon = pokemonGeoPoint!!.pokemon
                pokeGeoPoint = pokemonGeoPoint!!.pokeGeoPoint
            }

        }
        if (intent.extras!!.containsKey(keyTypeDetail)) {
            when (intent.extras!!.getString(keyTypeDetail)!!.lowercase()) {
                "me" -> {
                    isCaptured = true
                    isWild = false
                    isOther = false
                }
                "wild" -> {
                    isCaptured = false
                    isWild = true
                    isOther = false
                }
                "friends", "foes" -> {
                    isCaptured = false
                    isWild = false
                    isOther = true
                }
                else -> {
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.detailPokemon().collect { response ->
                if (response != null) {
                    when (response) {
                        is MyResponse.SuccessResponse<*> -> {
                            val pokeDetail = response.data as DetailPokemon
                            withContext(Main) {
                                buildTypePokemonUI(pokeDetail.types)
                                buildMovesPokemonUI(pokeDetail.moves)
                            }
                        }
                        else -> {

                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.responseAddedPokemon().collect { response ->
                if (response != null) {

                    when (response) {
                        is MyResponse.SuccessResponse<*> -> {
                            val isAdded = response.data as Boolean

                        }
                        else -> {

                        }
                    }
                    if (progress_dialog!!.isShowing) {
                        progress_dialog?.dismiss()
                        progress_dialog = null
                    }
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        if (!isOther) {
            val tileProvider =
                MapTileProviderBasic(applicationContext, TileSourceFactory.MAPNIK)
            val mTileRequestCompleteHandler = SimpleInvalidationHandler(mapView)
            tileProvider.setTileRequestCompleteHandler(mTileRequestCompleteHandler)
            mapView.tileProvider = tileProvider
            mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isOther) {
            mapView.onResume()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun showLoadingDialog(context: Context) {
        if (progress_dialog != null) {
            progress_dialog?.cancel()
        }
        progress_dialog = Dialog(context)
        val binding = AddNewPokemonDialogLoadingBinding.inflate(LayoutInflater.from(context))
        progress_dialog!!.setContentView(binding.root)
        // load the animation
        val animFade = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_anim
        )
        val animTransition = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.translate_anim
        )
        val animShake = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.shake_anim
        )
        val anim = AnimationSet(false)
        anim.addAnimation(animShake)
        anim.addAnimation(animTransition)
        binding.idPokeBallLoading.startAnimation(anim)
        binding.idPokeBPointRed.startAnimation(animFade)

        progress_dialog!!.setCancelable(false)
        progress_dialog!!.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        progress_dialog?.create()
        progress_dialog?.show()
    }

    companion object {

        const val keyTypeDetail = "keyTypePoke"
        const val keyPoke = "keyPoke"
        const val keyMePoke = "keyMePoke"
        const val keyPokeUser = "keyPokeUser"

        fun openDetailPokemonActivity(packageContext: Context) {
            val intent = Intent(packageContext, DetailPokemonActivity::class.java)
            packageContext.startActivity(intent)
        }

        fun openDetailPokemonActivityWithArgs(
            packageContext: Context,
            vararg args: Pair<String, Any>
        ) {
            val intent = Intent(packageContext, DetailPokemonActivity::class.java)
            val bundle = bundleOf(
                *args
            )
            intent.putExtras(bundle)
            packageContext.startActivity(intent)
        }

        fun openDetailPokemonActivityWithArgsAndHeroAnimation(
            packageContext: Context,
            activityOptions: ActivityOptions,
            vararg args: Pair<String, Any>
        ) {
            val intent = Intent(packageContext, DetailPokemonActivity::class.java)
            val bundle = bundleOf(
                *args
            )
            intent.putExtras(bundle)
            packageContext.startActivity(intent, activityOptions.toBundle())
        }
    }

    override fun captureNewPoke(pokemonWithGeoPoint: PokemonWithGeoPoint) {
    //    viewModel.addPokemonToMyTeam(pokemonWithGeoPoint)
        showLoadingDialog(this)
    }
}
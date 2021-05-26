package dali.hamza.pokemongofight.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.common.enabled
import dali.hamza.pokemongofight.common.gone
import dali.hamza.pokemongofight.common.visible
import dali.hamza.pokemongofight.databinding.EnterNameCapturePokeFragmentBinding
import dali.hamza.pokemongofight.databinding.GotchupPokemanLayoutBinding
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.lang.Exception

class GotchUpPokeDialog : DialogFragment() {

    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var myDialog: AlertDialog
    private lateinit var binding: GotchupPokemanLayoutBinding
    private lateinit var submitButton: MaterialButton
    private lateinit var pokemon: Pokemon
    private lateinit var konfettiView: KonfettiView
    private lateinit var textCograt: TextView

    companion object {
        const val tagChangePwdDialog = "CapturedPokeDialogTag"
        const val keyPokemonAdded = "pokemonAdded"
        fun newInstance(pokemon: Pokemon): GotchUpPokeDialog {
            return GotchUpPokeDialog().apply {
                arguments = Bundle().apply {
                    this.putParcelable(keyPokemonAdded, pokemon)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemon = requireArguments().get(keyPokemonAdded) as Pokemon
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(requireContext())
        binding = GotchupPokemanLayoutBinding.inflate(requireActivity().layoutInflater)

        setupUI()

        submitButton.setOnClickListener {

            dismiss()
        }

        builder.setView(
            binding.root
        )
        // Create the AlertDialog object and return it
        myDialog = builder.create()
        return myDialog
    }

    private fun setupUI() {
        konfettiView = binding.idGotchupViewKonfetti
        submitButton = binding.idCloseGotchup
        textCograt = binding.idTextCapturedPoke
        submitButton.enabled()


        textCograt.text = "Congratulation!\n${pokemon.name} is added to your team"

        Picasso.get()
            .load("https://pokeres.bastionbot.org/images/pokemon/${pokemon.id}.png")
            .into(binding.idPokemonPicked,object :Callback {
                override fun onSuccess() {
                    konfettiView.visible()
                    konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square, Shape.Circle)
                        .addSizes(Size(12, 5f))
                        .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
                        .streamFor(300, 3500L)
                    konfettiView.setOnClickListener {
                        konfettiView.stopGracefully()
                        konfettiView.gone()
                    }
                }

                override fun onError(e: Exception?) {
                    TODO("Not yet implemented")
                }

            })
    }

}
package dali.hamza.pokemongofight.ui.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.pokemongofight.common.enabled
import dali.hamza.pokemongofight.databinding.EnterNameCapturePokeFragmentBinding

class CapturePokeDialog constructor(
    private val action: CapturePokeCallback,
): DialogFragment() {

    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var myDialog: AlertDialog
    private lateinit var binding: EnterNameCapturePokeFragmentBinding
    private lateinit var namePokeInputLayout: TextInputLayout
    private lateinit var submitButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var pokemon: PokemonWithGeoPoint

    companion object {
        const val tagChangePwdDialog = "CapturePokeDialogTag"
        const val keyPokemonToAdd = "pokemonToAdd"
        fun newInstance(pokemon: PokemonWithGeoPoint, action:CapturePokeCallback):CapturePokeDialog {
           return  CapturePokeDialog(action).apply {
                arguments = Bundle().apply {
                    this.putParcelable(keyPokemonToAdd, pokemon)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemon = requireArguments().get(keyPokemonToAdd) as PokemonWithGeoPoint
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = MaterialAlertDialogBuilder(requireContext())
        binding = EnterNameCapturePokeFragmentBinding.inflate(requireActivity().layoutInflater)

        setupUI()

        submitButton.setOnClickListener {
            val input = namePokeInputLayout.editText!!.text.toString()
            val name = when (input.isEmpty()) {
                true -> pokemon.pokemon.name
                else -> input
            }
            val poke = pokemon.copy(
                pokemon = pokemon.pokemon.copy(
                    name = name
                )
            )
            action.captureNewPoke(poke)
            dismiss()
        }
        cancelButton.setOnClickListener {
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
        namePokeInputLayout = binding.idTextInputNamePokemon
        submitButton = binding.idBtSavePoke
        cancelButton = binding.idBtCancelSavePoke

        submitButton.enabled()
    }
    interface  CapturePokeCallback {
        fun captureNewPoke(pokemonWithGeoPoint: PokemonWithGeoPoint);
    }
}
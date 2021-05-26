package dali.hamza.pokemongofight.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dali.hamza.core.interactors.CapturePokemonUseCase
import dali.hamza.core.interactors.CheckTokenValidationUseCase
import dali.hamza.core.interactors.DetailPokemonUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokeError
import dali.hamza.domain.models.PokemonWithGeoPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPokemonViewModel @Inject constructor(
    private val capturePokemonUseCase: CapturePokemonUseCase,
    private val detailPokemonUseCase: DetailPokemonUseCase,
) : ViewModel() {
    private val mutableFlowDetailPoke: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val flowDetailPoke: StateFlow<IResponse?> = mutableFlowDetailPoke

    fun detailPokemon() = flowDetailPoke

    private val mutableFlowAddedPokemon: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val flowAddedPoke: StateFlow<IResponse?> = mutableFlowAddedPokemon

    fun responseAddedPokemon() = flowAddedPoke


    fun retrieveDetailPoke(id: Int) {
        viewModelScope.launch(IO) {
            detailPokemonUseCase.invoke(id)
                .catch {
                    mutableFlowDetailPoke.value =
                        MyResponse.ErrorResponse<String>(error = PokeError("error"))

                }.collect { response ->
                    mutableFlowDetailPoke.value = response
                }
        }
    }

    fun addPokemonToMyTeam(pokemonWithGeoPoint: PokemonWithGeoPoint) {
        viewModelScope.launch {
            capturePokemonUseCase.invoke(pokemonWithGeoPoint)
                .catch {
                    mutableFlowAddedPokemon.value =
                        MyResponse.ErrorResponse<Any>(PokeError("Cannot Added Pokemon"))
                }
                .collect { response ->
                    mutableFlowAddedPokemon.value = response
                }
        }
    }


}
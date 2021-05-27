package dali.hamza.pokemongofight.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dali.hamza.core.interactors.CheckTokenValidationUseCase
import dali.hamza.core.interactors.GetCapturedPokemonUseCase
import dali.hamza.core.interactors.GetCommunityPokemonsUseCase
import dali.hamza.core.interactors.GetMyTeamPokemonUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CapturedViewModel @Inject constructor(
    private val checkTokenValidationUseCase: CheckTokenValidationUseCase,
    private val getMyCapturedPokemonUseCase: GetCapturedPokemonUseCase
) : ViewModel() {


    private val mutableFlowCaptured: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val flowMyTeam: StateFlow<IResponse?> = mutableFlowCaptured

    fun getFlowCapturedPokemon() = flowMyTeam

    fun fetchForCapturedPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                checkTokenValidationUseCase.invoke(null)
            }
            getMyCapturedPokemonUseCase.invoke()
                .catch {

                }.collect { response ->
                    mutableFlowCaptured.value = response
                }

        }
    }

}
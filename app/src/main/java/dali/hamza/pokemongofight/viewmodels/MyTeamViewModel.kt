package dali.hamza.pokemongofight.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dali.hamza.core.interactors.CheckTokenValidationUseCase
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
class MyTeamViewModel @Inject constructor(
    private val checkTokenValidationUseCase: CheckTokenValidationUseCase,
    private val getMyTeamPokemonsUseCase: GetMyTeamPokemonUseCase
) : ViewModel() {


    private val mutableFlowMyTeam: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val flowMyTeam: StateFlow<IResponse?> = mutableFlowMyTeam

    fun getFlowCommunityPokemon() = flowMyTeam

    fun fetchForCommunityPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                checkTokenValidationUseCase.invoke(null)
            }
            getMyTeamPokemonsUseCase.invoke()
                .catch {

                }.collect { response ->
                    mutableFlowMyTeam.value = response
                }

        }
    }

}
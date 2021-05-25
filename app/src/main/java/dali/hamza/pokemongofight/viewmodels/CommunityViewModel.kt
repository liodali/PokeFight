package dali.hamza.pokemongofight.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dali.hamza.core.interactors.CheckTokenValidationUseCase
import dali.hamza.core.interactors.GetCommunityPokemonsUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
   private val checkTokenValidationUseCase: CheckTokenValidationUseCase,
    val getCommunityPokemonsUseCase: GetCommunityPokemonsUseCase
) : ViewModel() {


    private val mutableFlowCommunity: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val flowCommunity: StateFlow<IResponse?> = mutableFlowCommunity

    fun getFlowCommunityPokemon() = flowCommunity

    fun fetchForCommunityPokemon() {
        viewModelScope.launch(IO) {
            withContext(IO) {
                checkTokenValidationUseCase.invoke(null)
            }
            getCommunityPokemonsUseCase.invoke()
                .catch {
                    mutableFlowCommunity.value = MyResponse.ErrorResponse<Any>(PokeError("no data"))
                }
                .collect { response ->
                    mutableFlowCommunity.value = response
                }
        }
    }


}
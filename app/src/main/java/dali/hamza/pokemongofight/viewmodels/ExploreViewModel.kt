package dali.hamza.pokemongofight.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dali.hamza.core.interactors.GetRandomListPokemonUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.pokemongofight.common.toPokemonGeoPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val randomListPokemonUseCase: GetRandomListPokemonUseCase
) : ViewModel() {

    private val listPokemonMutableFlow: MutableStateFlow<IResponse?> = MutableStateFlow(null)
    private val listPokemonFlow: StateFlow<IResponse?> = listPokemonMutableFlow


    fun getListPokemon() = listPokemonFlow

    fun exploreListPokemon(
        userGeoPoint: GeoPoint
    ) {
        viewModelScope.launch(IO) {
            randomListPokemonUseCase.invoke(
                1000, userGeoPoint.toPokemonGeoPoint()
            ).catch {
                Log.e("error","error")
            }.collect { response ->
                listPokemonMutableFlow.value = response
            }
        }
    }
}
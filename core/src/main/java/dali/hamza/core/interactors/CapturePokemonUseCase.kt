package dali.hamza.core.interactors

import dali.hamza.domain.interactor.FlowIResponseUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.PokemonWithGeoPoint
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CapturePokemonUseCase @Inject constructor(
    private val repository: IPokemonRepository
) : FlowIResponseUseCase<PokemonWithGeoPoint> {
    override suspend fun invoke(parameter: PokemonWithGeoPoint?): Flow<IResponse> {
        return repository.insert(parameter!!)
    }
}
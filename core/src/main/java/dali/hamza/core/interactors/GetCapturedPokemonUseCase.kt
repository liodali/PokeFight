package dali.hamza.core.interactors

import dali.hamza.domain.interactor.FlowIResponseUseCase0
import dali.hamza.domain.interactor.FlowUseCase0
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCapturedPokemonUseCase @Inject constructor(
   private val repository: IPokemonRepository
): FlowIResponseUseCase0 {
    override suspend fun invoke(): Flow<IResponse> {
        return repository.getAllFlow("Captured")
    }
}
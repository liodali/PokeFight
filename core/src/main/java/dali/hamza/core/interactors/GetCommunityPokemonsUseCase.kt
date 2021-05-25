package dali.hamza.core.interactors

import dali.hamza.domain.interactor.FlowIResponseUseCase0
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommunityPokemonsUseCase @Inject constructor(
    private  val repository: IPokemonRepository
) : FlowIResponseUseCase0 {
    override suspend fun invoke(): Flow<IResponse> = repository.getCommunityPokemonFlow()
}
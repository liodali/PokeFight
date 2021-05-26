package dali.hamza.core.interactors

import dali.hamza.domain.interactor.FlowIResponseUseCase
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailPokemonUseCase @Inject constructor(
    private val repository: IPokemonRepository
): FlowIResponseUseCase<Int> {
    override suspend fun invoke(parameter: Int?): Flow<IResponse> {
        return  repository.getOneFlow(parameter!!)
    }
}
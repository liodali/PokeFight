package dali.hamza.core.interactors

import dali.hamza.domain.interactor.FlowIResponseUseCases
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomListPokemonUseCase @Inject constructor(
    private val repository: IPokemonRepository
) : FlowIResponseUseCases<Any> {
    override suspend fun invoke(vararg parameters: Any?): Flow<IResponse> {
        return repository.getRandomPokemonLocationFlow(
            parameters.last() as PokeGeoPoint
        )
    }
}
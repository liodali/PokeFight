package dali.hamza.core.interactors

import dali.hamza.domain.interactor.VoidFlowUseCase
import dali.hamza.domain.repository.IAppRepository
import javax.inject.Inject

class CheckTokenValidationUseCase @Inject constructor(
    private val repository: IAppRepository
) : VoidFlowUseCase<String> {
    override suspend fun invoke(parameter: String?) {
        val validToken = repository.checkTokenValidity()
        if (!validToken) {
            repository.grantAuthorizationToken()
        }
    }
}
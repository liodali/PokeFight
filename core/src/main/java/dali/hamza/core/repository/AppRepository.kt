package dali.hamza.core.repository

import dali.hamza.core.datasource.network.AuthorizationApi
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.core.utilities.DateManager
import dali.hamza.core.utilities.SessionManager
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.repository.IAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class AppRepository @Inject constructor(
    var api: AuthorizationApi
) : IAppRepository {

    @Inject
    lateinit var sessionManager: SessionManager

    override suspend fun checkTokenValidity(): Boolean {
        val expired = sessionManager.getExpirationFromDataStore.first()
        val date = DateManager.difference2Date(Date(expired))
        if (date.minutes < 45 && date.hours < 1) {
            return false
        }
        return true
    }

    override suspend fun grantAuthorizationToken(email: String) {
        val token = api.getToken(email).data {
            it
        }
        when (token) {
            is MyResponse.SuccessResponse -> {
                sessionManager.saveToken(token.data!!.token)
                sessionManager.saveExpirationDAte(token.data!!.expiredAt)
            }
            else -> throw Exception("no authorized")

        }
    }

    override suspend fun getToken(): Flow<String> = sessionManager.getTokenFromDataStore
}
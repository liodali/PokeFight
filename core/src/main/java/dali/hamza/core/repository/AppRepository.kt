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
import javax.inject.Named

class AppRepository @Inject constructor(
    @Named("AuthorizationApi") var api: AuthorizationApi,
) : IAppRepository {

    @Inject
    lateinit var sessionManager: SessionManager


//    @Inject
//    @Named("EmailUser")
//    lateinit var EmailUser: String

    override suspend fun checkTokenValidity(): Boolean {
        /**
         * check if token exist or not
         */
        if (sessionManager.getTokenFromDataStore.first().isEmpty()) {
            return false
        }
        /**
         * check if token valid or not
         */
        val expired = sessionManager.getExpirationFromDataStore.first()
        if (expired == 0L) {
            return false
        }
        val date = DateManager.difference2Date(Date(expired))
        if (date.minutes < 45 && date.hours < 1) {
            return true
        }
        return false
    }

    override suspend fun grantAuthorizationToken() {
        val token = api.getToken("hamza.mohamedali93@gmail.com").data {
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

}
package dali.hamza.domain.repository

import kotlinx.coroutines.flow.Flow

interface IAppRepository {

    suspend fun checkTokenValidity(): Boolean
    suspend fun grantAuthorizationToken(email: String)
    suspend fun getToken(): Flow<String>

}
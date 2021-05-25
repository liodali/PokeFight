package dali.hamza.core.datasource.network

import dali.hamza.core.datasource.network.models.TokenAuthorization
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationApi {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @POST("token")
    suspend fun getToken(
        @Query("email") email: String
    ): Response<TokenAuthorization>
}
package dali.hamza.core.datasource.network.models

import com.squareup.moshi.Json
import java.util.*

data class TokenAuthorization(
    @Json(name = "token") val token: String,
    @Json(name = "expiresAt") val expiredAt: Long
)

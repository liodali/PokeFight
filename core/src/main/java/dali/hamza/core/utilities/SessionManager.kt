package dali.hamza.core.utilities

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SessionManager constructor(
    @ApplicationContext private val context: Context
) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "music_pref"
    )


    companion object {
        val TokenKey = stringPreferencesKey("Token")
        val expireAtKey = longPreferencesKey("expiredAt")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
              preferences[TokenKey] = token
        }
    }

    val getTokenFromDataStore: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[TokenKey] ?: ""
        }
    suspend fun saveExpirationDAte(date: Long) {
        context.dataStore.edit { preferences ->
            preferences[expireAtKey] = date
        }
    }

    val getExpirationFromDataStore: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[expireAtKey] ?: 0L
        }

}
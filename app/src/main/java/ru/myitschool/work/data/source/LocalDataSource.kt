package ru.myitschool.work.data.source

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.myitschool.work.App
import ru.myitschool.work.core.jsonCore
import ru.myitschool.work.domain.auth.entities.LocalAuthInfo
import ru.myitschool.work.utils.security.AppSecurityProvider

object LocalDataSource {
    private object AuthPreferences {
        val AUTH_INFO_KEY = stringPreferencesKey("AUTH_INFO_KEY")
    }

    private val aead = AppSecurityProvider.provideAead()
    private val Context.authSecuredDatastore: DataStore<Preferences> by preferencesDataStore(name = "AUTH_STORE")

    val authInfoFlow: Flow<LocalAuthInfo?> = App.context.authSecuredDatastore.data.map { preferences ->
        val b64 = preferences[AuthPreferences.AUTH_INFO_KEY] ?: return@map null

        try {
            val encrypted = Base64.decode(b64, Base64.NO_WRAP)
            val decrypted = aead.decrypt(encrypted, null)
            jsonCore.decodeFromString<LocalAuthInfo>(decrypted.decodeToString())
        } catch (e: Exception) {
            Log.e("SecureAuthInfoStorage", "Failed to load auth info: ${e.message}", e)
            return@map null
        }
    }

    suspend fun saveAuthInfo(info: LocalAuthInfo) {
        val json = jsonCore.encodeToString(info)
        val encrypted = aead.encrypt(json.toByteArray(), null)
        val b64 = Base64.encodeToString(encrypted, Base64.NO_WRAP)

        App.context.authSecuredDatastore.edit {
            it[AuthPreferences.AUTH_INFO_KEY] = b64
        }
    }

    suspend fun loadLocalAuthInfo(): LocalAuthInfo? {
        return authInfoFlow.firstOrNull()
    }

    suspend fun clearAuthInfo() {
        App.context.authSecuredDatastore.edit {
            it.remove(AuthPreferences.AUTH_INFO_KEY)
        }
    }
}

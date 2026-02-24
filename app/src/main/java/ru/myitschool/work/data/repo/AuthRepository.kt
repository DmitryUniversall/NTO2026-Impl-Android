package ru.myitschool.work.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import ru.myitschool.work.App
import ru.myitschool.work.data.source.NetworkDataSource

object AuthRepository {
    private const val STORE = "AUTH-STORE"
    private const val CODE_KEY = "CODE"

    private var codeCache: String? = null

    suspend fun checkAndSave(text: String): Result<Boolean> {
        return NetworkDataSource.checkAuth(text).onSuccess { success ->
            if (success) {
                codeCache = text
                App.context.userDataStore.edit { preferences ->
                    val prefKey = stringPreferencesKey(CODE_KEY)
                    preferences[prefKey] = text
                }
            }
        }
    }

    suspend fun getCode(): String? {
        if (codeCache == null) {
            codeCache = App.context.userDataStore.data
                .firstOrNull()
                ?.let { preferences ->
                    preferences[stringPreferencesKey(CODE_KEY)]
                }
        }
        return codeCache
    }

    suspend fun logout() {
        codeCache = null
        App.context.userDataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(CODE_KEY)
            preferences.remove(prefKey)
        }
    }

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = STORE)
}
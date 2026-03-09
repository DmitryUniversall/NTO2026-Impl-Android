package ru.myitschool.work.data.repo

import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import ru.myitschool.work.data.source.LocalDataSource
import ru.myitschool.work.data.source.NetworkDataSource
import ru.myitschool.work.domain.auth.entities.AuthState
import ru.myitschool.work.domain.auth.entities.LocalAuthInfo

object AuthRepository {
    private val _authStateFlow = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState = _authStateFlow.asStateFlow()

    fun generateBasicAuthToken(login: String, password: String): String {
        val credentials = "$login:$password"
        return Base64.encodeToString(credentials.toByteArray(Charsets.UTF_8), Base64.DEFAULT).replace("\n", "")
    }

    suspend fun restoreAuthState(): AuthState {
        val authInfo = LocalDataSource.loadLocalAuthInfo()
            ?: return AuthState.Unauthenticated

        val token = authInfo.basicToken

        val dto = NetworkDataSource.login(token).getOrThrow()

        val state = AuthState.Authenticated(
            basicToken = token,
            user = dto.toUser()
        )

        _authStateFlow.emit(state)
        return state
    }

    suspend fun login(login: String, password: String): Result<AuthState.Authenticated> {
        return NetworkDataSource.login(generateBasicAuthToken(login, password)).mapCatching { dto ->
            val state = AuthState.Authenticated(
                basicToken = generateBasicAuthToken(login, password),
                user = dto.toUser()
            )

            _authStateFlow.emit(state)

            LocalDataSource.saveAuthInfo(
                LocalAuthInfo(
                    basicToken = state.basicToken
                )
            )

            state
        }
    }

    suspend fun reLogin(): Result<AuthState.Authenticated> {
        return NetworkDataSource.reLogin().mapCatching { dto ->
            val state = AuthState.Authenticated(
                basicToken = (_authStateFlow.first() as AuthState.Authenticated).basicToken,
                user = dto.toUser()
            )

            _authStateFlow.emit(state)

            LocalDataSource.saveAuthInfo(
                LocalAuthInfo(
                    basicToken = state.basicToken
                )
            )

            state
        }
    }

    suspend fun getAuthToken(): String? {
        val state = authState.firstOrNull()
        if (state !is AuthState.Authenticated) return null
        return state.basicToken
    }

    suspend fun clearAuthInfo() {
        _authStateFlow.emit(AuthState.Unauthenticated)
        LocalDataSource.clearAuthInfo()
    }

    suspend fun logout() {
        clearAuthInfo()
    }
}

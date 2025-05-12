package com.coding.meet.newsapp.data.auth

import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.ForgotPasswordResponse
import com.coding.meet.newsapp.domain.auth.model.NewUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {

    override val isAuthenticated: Flow<Boolean> =
        authLocalDataSource.getToken().map { it != null }

    override suspend fun login(credentials: Credentials): AuthResult<Unit> {
        return try {
            val response = authApi.login(credentials)
            if (response.success && response.token != null) {
                authLocalDataSource.saveToken(response.token)
                AuthResult.Success(Unit)
            } else {
                AuthResult.Error(response.message ?: "Unknown login error")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error during login")
        }
    }

    override suspend fun register(newUser: NewUser): AuthResult<Unit> {
        return try {
            val response = authApi.register(newUser)
            if (response.success && response.token != null) {
                // Quyết định: tự động login sau khi đăng ký hoặc yêu cầu login lại
                // authLocalDataSource.saveToken(response.token) // Tự động login
                AuthResult.Success(Unit)
            } else {
                AuthResult.Error(response.message ?: "Unknown registration error")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error during registration")
        }
    }

    override suspend fun forgotPassword(email: String): AuthResult<ForgotPasswordResponse> {
        return try {
            val response = authApi.forgotPassword(email)
            if (response.success) {
                AuthResult.Success(response)
            } else {
                AuthResult.Error(response.message ?: "Unknown forgot password error")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error during forgot password")
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        return try {
            authLocalDataSource.clearToken()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error clearing token")
        }
    }
}
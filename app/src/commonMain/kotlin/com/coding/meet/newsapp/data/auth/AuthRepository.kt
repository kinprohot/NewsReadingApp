package com.coding.meet.newsapp.data.auth

import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.ForgotPasswordResponse
import com.coding.meet.newsapp.domain.auth.model.NewUser
import kotlinx.coroutines.flow.Flow

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
}

interface AuthRepository {
    val isAuthenticated: Flow<Boolean>
    suspend fun login(credentials: Credentials): AuthResult<Unit>
    suspend fun register(newUser: NewUser): AuthResult<Unit>
    suspend fun forgotPassword(email: String): AuthResult<ForgotPasswordResponse>
    suspend fun logout(): AuthResult<Unit>
}
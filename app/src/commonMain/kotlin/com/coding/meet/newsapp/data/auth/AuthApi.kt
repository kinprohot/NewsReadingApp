package com.coding.meet.newsapp.data.auth

import com.coding.meet.newsapp.domain.auth.model.AuthResponse
import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.ForgotPasswordResponse
import com.coding.meet.newsapp.domain.auth.model.NewUser
import kotlinx.coroutines.delay // Thêm import

interface AuthApi {
    suspend fun login(credentials: Credentials): AuthResponse
    suspend fun register(newUser: NewUser): AuthResponse
    suspend fun forgotPassword(email: String): ForgotPasswordResponse
}

class FakeAuthApi : AuthApi {
    override suspend fun login(credentials: Credentials): AuthResponse {
        delay(1000)
        return if (credentials.email == "test@example.com" && credentials.password == "password") {
            AuthResponse(token = "fake_token_12345", userId = "user_abc", success = true, message = "Login successful")
        } else {
            AuthResponse(success = false, message = "Invalid credentials")
        }
    }

    override suspend fun register(newUser: NewUser): AuthResponse {
        delay(1500)
        return AuthResponse(token = "fake_new_user_token_${newUser.username}", userId = "user_${System.currentTimeMillis()}", success = true, message = "Registration successful for ${newUser.username}")
    }

    override suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        delay(1000)
        return ForgotPasswordResponse(success = true, message = "Password reset link sent to $email")
    }
}
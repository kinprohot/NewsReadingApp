package com.coding.meet.newsapp.domain.auth.model

import kotlinx.serialization.Serializable

// Request models
@Serializable
data class Credentials(
    val email: String,
    val password: String
)

@Serializable
data class NewUser(
    val username: String,
    val email: String,
    val password: String
)

// Response models
@Serializable
data class AuthResponse(
    val token: String? = null,
    val userId: String? = null,
    val message: String? = null,
    val success: Boolean
)

@Serializable
data class ForgotPasswordResponse(
    val message: String,
    val success: Boolean
)

// Domain models (representation for UI/use cases)
data class User(
    val id: String,
    val email: String,
    val username: String? = null
)
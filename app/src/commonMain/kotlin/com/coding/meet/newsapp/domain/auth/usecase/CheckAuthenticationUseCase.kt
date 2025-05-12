package com.coding.meet.newsapp.domain.auth.usecase

import com.coding.meet.newsapp.data.auth.AuthRepository
import kotlinx.coroutines.flow.Flow

class CheckAuthenticationUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.isAuthenticated
    }
}
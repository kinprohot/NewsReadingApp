package com.coding.meet.newsapp.ui.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.meet.newsapp.data.auth.AuthRepository
import com.coding.meet.newsapp.data.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null, successMessage = null) }
    }

    fun onSendResetLinkClick() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email.trim()

        if (email.isEmpty()) {
            _uiState.update { it.copy(error = "Email cannot be empty.") }
            return
        }
        // Thêm validation định dạng email

        _uiState.update { it.copy(isLoading = true, error = null, successMessage = null) }

        viewModelScope.launch {
            val result = authRepository.forgotPassword(email)
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, successMessage = result.data.message, error = null) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message, successMessage = null) }
                }
                AuthResult.Loading -> {}
            }
        }
    }
    fun onMessageHandled() { // Gọi sau khi thông báo đã được hiển thị
        _uiState.update { it.copy(successMessage = null, error = null) }
    }
}
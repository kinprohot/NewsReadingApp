package com.coding.meet.newsapp.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.meet.newsapp.data.auth.AuthRepository
import com.coding.meet.newsapp.data.auth.AuthResult
import com.coding.meet.newsapp.domain.auth.model.NewUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, error = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }

    fun onRegisterClick() {
        if (_uiState.value.isLoading) return

        val username = _uiState.value.username.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.update { it.copy(error = "All fields are required.") }
            return
        }
        if (password != confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match.") }
            return
        }
        // Thêm validation khác nếu cần (độ dài mật khẩu, ký tự đặc biệt...)

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = authRepository.register(NewUser(username, email, password))
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, registrationSuccess = true, error = null) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message, registrationSuccess = false) }
                }
                AuthResult.Loading -> {}
            }
        }
    }

    fun onRegistrationHandled() {
        _uiState.update { it.copy(registrationSuccess = false) }
    }
}
package com.coding.meet.newsapp.ui.auth.login

import androidx.lifecycle.ViewModel // Sử dụng AndroidX ViewModel
import androidx.lifecycle.viewModelScope // Sử dụng viewModelScope
import com.coding.meet.newsapp.data.auth.AuthRepository
import com.coding.meet.newsapp.data.auth.AuthResult
import com.coding.meet.newsapp.domain.auth.model.Credentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() { // Kế thừa từ ViewModel

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onLoginClick() {
        if (_uiState.value.isLoading) return

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(error = "Email and password cannot be empty.") }
            return
        }
        // Thêm validation khác nếu cần (ví dụ: định dạng email)

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch { // Sử dụng viewModelScope
            val result = authRepository.login(Credentials(email, password))
            when (result) {
                is AuthResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true, error = null) }
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message, loginSuccess = false) }
                }
                AuthResult.Loading -> {
                    // Trạng thái loading đã được set ở trên
                }
            }
        }
    }

    fun onLoginHandled() { // Gọi sau khi điều hướng thành công
        _uiState.update { it.copy(loginSuccess = false) }
    }
}
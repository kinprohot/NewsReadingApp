package com.coding.meet.newsapp.ui.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.coding.meet.newsapp.ui.common.component.AuthInput

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onRegistrationSuccess: () -> Unit, // Callback để điều hướng (ví dụ: về Login)
    onGoToLogin: () -> Unit
) {
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegistrationSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(32.dp))

        AuthInput(
            value = uiState.username,
            onValueChange = onUsernameChange,
            label = "Username",
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            isError = !uiState.error.isNullOrEmpty() // Hoặc lỗi cụ thể cho trường này
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInput(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = !uiState.error.isNullOrEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInput(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            isError = !uiState.error.isNullOrEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInput(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirm Password",
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onRegisterClick() }), // Ví dụ: gọi register khi nhấn Done
            isError = !uiState.error.isNullOrEmpty()
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
                Text("Register")
            }
        }

        uiState.error?.let {
            Text(it, color = MaterialTheme.colors.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToLogin) {
            Text("Already have an account? Login")
        }
    }
}
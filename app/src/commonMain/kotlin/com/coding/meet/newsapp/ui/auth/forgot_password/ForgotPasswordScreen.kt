package com.coding.meet.newsapp.ui.auth.forgot_password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.coding.meet.newsapp.ui.common.component.AuthInput

@Composable
fun ForgotPasswordScreen(
    uiState: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSendResetLinkClick: () -> Unit,
    onGoToLogin: () -> Unit,
    onMessageShown: () -> Unit // Callback khi thông báo thành công/lỗi đã được hiển thị
) {
    LaunchedEffect(uiState.successMessage, uiState.error) {
        if (uiState.successMessage != null || uiState.error != null) {
            // Có thể thêm delay nhỏ ở đây trước khi gọi onMessageShown nếu muốn
            // Hoặc để UI tự quyết định khi nào gọi (ví dụ: sau khi Snackbar biến mất)
            // onMessageShown() // Hoặc bạn có thể không cần callback này nếu UI tự xử lý
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Forgot Password", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Enter your email address and we will send you a link to reset your password.",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        AuthInput(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onSendResetLinkClick() }),
            isError = !uiState.error.isNullOrEmpty()
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = onSendResetLinkClick, modifier = Modifier.fillMaxWidth()) {
                Text("Send Reset Link")
            }
        }

        uiState.successMessage?.let {
            Text(it, color = MaterialTheme.colors.primary, modifier = Modifier.padding(top = 8.dp))
            // Gọi onMessageShown() ở đây nếu muốn reset message sau khi hiển thị
            // Hoặc để ViewModel tự reset message sau một khoảng thời gian/sự kiện
        }

        uiState.error?.let {
            Text(it, color = MaterialTheme.colors.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onGoToLogin) {
            Text("Back to Login")
        }
    }
}
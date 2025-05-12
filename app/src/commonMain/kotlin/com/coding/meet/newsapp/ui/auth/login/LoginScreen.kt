package com.coding.meet.newsapp.ui.auth.login

import androidx.compose.foundation.layout.*
// Các import cho KeyboardOptions và các lớp liên quan
import androidx.compose.foundation.text.KeyboardActions // Thêm nếu bạn muốn xử lý actions
import androidx.compose.foundation.text.KeyboardOptions // Import chính
import androidx.compose.material.*
import androidx.compose.material.icons.Icons // Thêm để sử dụng icons
import androidx.compose.material.icons.filled.Email // Icon cho email
import androidx.compose.material.icons.filled.Lock // Icon cho password
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction // Cho nút action trên bàn phím
import androidx.compose.ui.text.input.KeyboardType // Cho loại bàn phím (Email, Password, Text)
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation // Import VisualTransformation
import androidx.compose.ui.unit.dp
// Xóa import này nếu AuthInput đã ở trong file này:
// import com.coding.meet.newsapp.ui.common.component.AuthInput

// @Composable // Bỏ comment và dùng nếu bạn có Preview trong androidMain
// fun PreviewLoginScreen() { ... } // Di chuyển hàm này sang androidMain/preview nếu muốn giữ


@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    onGoToForgotPassword: () -> Unit
) {
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(32.dp))

        AuthInput(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, // Đặt loại bàn phím là Email
                imeAction = ImeAction.Next // Chuyển sang trường tiếp theo khi nhấn enter/next
            ),
            // Thêm keyboardActions nếu bạn muốn xử lý sự kiện Next cụ thể
            // keyboardActions = KeyboardActions(onNext = { /* focus next field */ }),
            isError = !uiState.error.isNullOrEmpty() // Ví dụ: hiển thị lỗi nếu có
        )
        Spacer(modifier = Modifier.height(16.dp))

        AuthInput(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
            visualTransformation = PasswordVisualTransformation(), // Ẩn mật khẩu
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, // Đặt loại bàn phím là Password
                imeAction = ImeAction.Done // Hoàn tất khi nhấn enter/done
            ),
            // Thêm keyboardActions nếu bạn muốn xử lý sự kiện Done cụ thể
            // keyboardActions = KeyboardActions(onDone = { onLoginClick() }),
            isError = !uiState.error.isNullOrEmpty()
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
        }

        uiState.error?.let {
            Text(it, color = MaterialTheme.colors.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onGoToRegister) {
            Text("Don't have an account? Register")
        }
        TextButton(onClick = onGoToForgotPassword) {
            Text("Forgot Password?")
        }
    }
}

@Composable
fun AuthInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default, // Sử dụng KeyboardOptions từ foundation.text
    keyboardActions: KeyboardActions = KeyboardActions.Default, // Thêm KeyboardActions
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions, // Truyền vào OutlinedTextField
        keyboardActions = keyboardActions, // Truyền vào OutlinedTextField
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            // Bạn có thể tùy chỉnh màu sắc ở đây nếu muốn
        )
    )
}

// Bạn có thể giữ hàm Preview này ở đây nếu LoginScreen.kt nằm trong androidMain/kotlin
// Nếu LoginScreen.kt nằm trong commonMain, hãy di chuyển PreviewLoginScreen sang
// một file preview riêng trong androidMain/kotlin/...
// Hoặc xóa nó đi nếu bạn không dùng preview.
@Composable
fun PreviewLoginScreen() { // Nếu giữ lại, thêm @Preview nếu file này ở androidMain
    // Cung cấp một Theme giả hoặc Theme thật của bạn cho Preview
    MaterialTheme { // Hoặc NewsAppTheme nếu bạn có
        LoginScreen(
            uiState = LoginUiState(error = "Sample error message"), // Ví dụ UI state
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onLoginSuccess = {},
            onGoToRegister = {},
            onGoToForgotPassword = {}
        )
    }
}
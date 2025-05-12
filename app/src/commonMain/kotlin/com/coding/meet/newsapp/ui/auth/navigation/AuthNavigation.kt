package com.coding.meet.newsapp.ui.auth.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation // Thêm import cho nested graph
import com.coding.meet.newsapp.ui.auth.forgot_password.ForgotPasswordScreen
import com.coding.meet.newsapp.ui.auth.forgot_password.ForgotPasswordViewModel
import com.coding.meet.newsapp.ui.auth.login.LoginScreen
import com.coding.meet.newsapp.ui.auth.login.LoginViewModel
import com.coding.meet.newsapp.ui.auth.register.RegisterScreen
import com.coding.meet.newsapp.ui.auth.register.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

object AuthRoutes {
    const val ROOT = "auth_root_graph" // Route cho nested graph
    const val LOGIN = "login_route"
    const val REGISTER = "register_route"
    const val FORGOT_PASSWORD = "forgot_password_route"
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onAuthSuccess: () -> Unit // Callback chung khi login/register thành công và cần điều hướng ra ngoài AuthGraph
) {
    navigation(
        route = AuthRoutes.ROOT, // Đặt tên cho nested graph
        startDestination = AuthRoutes.LOGIN // Màn hình bắt đầu của auth flow
    ) {
        composable(AuthRoutes.LOGIN) {
            val viewModel: LoginViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LoginScreen(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = viewModel::onLoginClick,
                onLoginSuccess = {
                    viewModel.onLoginHandled() // Reset flag trong ViewModel
                    onAuthSuccess()
                },
                onGoToRegister = { navController.navigate(AuthRoutes.REGISTER) },
                onGoToForgotPassword = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) }
            )
        }

        composable(AuthRoutes.REGISTER) {
            val viewModel: RegisterViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            RegisterScreen(
                uiState = uiState,
                onUsernameChange = viewModel::onUsernameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onRegisterClick = viewModel::onRegisterClick,
                onRegistrationSuccess = {
                    viewModel.onRegistrationHandled()
                    // Quyết định: điều hướng về Login hay vào thẳng app
                    navController.popBackStack(AuthRoutes.LOGIN, inclusive = false) // Quay về Login
                    // Hoặc: onAuthSuccess() // Nếu đăng ký xong là vào app luôn (cần logic auto-login trong repo)
                },
                onGoToLogin = {
                    navController.popBackStack(AuthRoutes.LOGIN, inclusive = false)
                }
            )
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            val viewModel: ForgotPasswordViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            ForgotPasswordScreen(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onSendResetLinkClick = viewModel::onSendResetLinkClick,
                onGoToLogin = {
                    navController.popBackStack(AuthRoutes.LOGIN, inclusive = false)
                },
                onMessageShown = viewModel::onMessageHandled
            )
        }
    }
}
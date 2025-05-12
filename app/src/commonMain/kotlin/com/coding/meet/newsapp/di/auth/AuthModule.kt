package com.coding.meet.newsapp.di.auth

// import com.coding.meet.newsapp.data.auth.AuthApi // Không cần AuthApi nữa nếu dùng Firebase trực tiếp
// import com.coding.meet.newsapp.data.auth.FakeAuthApi // Không dùng FakeAuthApi nữa
import com.coding.meet.newsapp.data.auth.AuthLocalDataSource
import com.coding.meet.newsapp.data.auth.AuthRepository
import com.coding.meet.newsapp.data.auth.AuthRepositoryImpl
import com.coding.meet.newsapp.domain.auth.service.FirebaseAuthService // Import service mới
import com.coding.meet.newsapp.domain.auth.usecase.CheckAuthenticationUseCase
import com.coding.meet.newsapp.ui.auth.forgot_password.ForgotPasswordViewModel
import com.coding.meet.newsapp.ui.auth.login.LoginViewModel
import com.coding.meet.newsapp.ui.auth.register.RegisterViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val authModuleCommon: Module = module {
    // FirebaseAuthService sẽ được cung cấp bởi firebaseAuthServiceModule() (expect/actual)
    // Không cần single<AuthApi> ở đây nữa

    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuthService = get<FirebaseAuthService>(), // Inject FirebaseAuthService
            authLocalDataSource = get<AuthLocalDataSource>()
        )
    }

    // ... (Domain Layer và UI Layer giữ nguyên)
    factory { CheckAuthenticationUseCase(get()) }
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { ForgotPasswordViewModel(get()) }
}
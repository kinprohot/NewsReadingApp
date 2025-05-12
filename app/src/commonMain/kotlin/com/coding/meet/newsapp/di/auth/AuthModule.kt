package com.coding.meet.newsapp.di.auth

import com.coding.meet.newsapp.data.auth.AuthApi
import com.coding.meet.newsapp.data.auth.AuthLocalDataSource // Interface
import com.coding.meet.newsapp.data.auth.AuthRepository
import com.coding.meet.newsapp.data.auth.AuthRepositoryImpl
import com.coding.meet.newsapp.data.auth.FakeAuthApi // Hoặc API thật
import com.coding.meet.newsapp.domain.auth.usecase.CheckAuthenticationUseCase
import com.coding.meet.newsapp.ui.auth.forgot_password.ForgotPasswordViewModel
import com.coding.meet.newsapp.ui.auth.login.LoginViewModel
import com.coding.meet.newsapp.ui.auth.register.RegisterViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val authModuleCommon: Module = module {
    // Data Layer
    single<AuthApi> { FakeAuthApi() } // Thay bằng API thật của bạn
    // AuthLocalDataSource (interface) sẽ được cung cấp bởi platform-specific module
    single<AuthRepository> { AuthRepositoryImpl(get(), get<AuthLocalDataSource>()) }

    // Domain Layer
    factory { CheckAuthenticationUseCase(get()) }

    // UI Layer (ViewModels) - Koin sẽ quản lý scope khi dùng với koinViewModel trên Android
    factory { LoginViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { ForgotPasswordViewModel(get()) }
}
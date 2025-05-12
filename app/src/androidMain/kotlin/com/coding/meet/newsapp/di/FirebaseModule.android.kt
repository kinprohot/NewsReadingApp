package com.coding.meet.newsapp.di // Hoặc com.coding.meet.newsapp.di.firebase

import com.coding.meet.newsapp.data.auth.service.FirebaseAuthServiceImpl
import com.coding.meet.newsapp.domain.auth.service.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Actual Koin module for Android providing Firebase-specific implementations.
 */
actual fun firebaseAuthServiceModule(): Module = module {
    // Cung cấp instance của FirebaseAuth từ Firebase Android SDK
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    // Cung cấp implementation của FirebaseAuthService cho Android
    single<FirebaseAuthService> { FirebaseAuthServiceImpl(get()) }
}
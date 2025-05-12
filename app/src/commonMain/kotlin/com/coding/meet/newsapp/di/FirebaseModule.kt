package com.coding.meet.newsapp.di // Hoặc com.coding.meet.newsapp.di.firebase

import com.coding.meet.newsapp.domain.auth.service.FirebaseAuthService
import org.koin.core.module.Module
import org.koin.dsl.bind // Thêm bind
import org.koin.dsl.module


/**
 * Expected declaration for a Koin module that provides the platform-specific
 * implementation of FirebaseAuthService.
 */
expect fun firebaseAuthServiceModule(): Module

// Module chung có thể khai báo rằng nó cần FirebaseAuthService
// Hoặc AuthRepositoryImpl sẽ trực tiếp inject FirebaseAuthService
// Trong trường hợp này, AuthRepositoryImpl sẽ thay thế FakeAuthApi bằng FirebaseAuthService
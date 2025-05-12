package com.coding.meet.newsapp.di.auth

import com.coding.meet.newsapp.data.auth.AuthLocalDataSource
import com.coding.meet.newsapp.data.auth.AuthLocalDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module specific to Android for the Auth feature.
 * Provides the Android-specific implementation of AuthLocalDataSource.
 */
val authModuleAndroid: Module = module {
    // Cung cấp implementation của AuthLocalDataSource cho Android.
    // AuthLocalDataSourceImpl cần một DataStore<Preferences> được inject,
    // instance DataStore này sẽ được cung cấp bởi một module khác trong platformSpecificModules().
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(get()) }
}
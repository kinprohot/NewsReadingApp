package com.coding.meet.newsapp.di

import org.koin.core.module.Module

/**
 * Expected declaration for a list of Koin modules that are platform-specific.
 * Each platform (android, ios, desktop) will provide its actual implementation.
 * These modules will typically provide platform-specific implementations for
 * interfaces defined in commonMain (like AuthLocalDataSource) and other
 * platform-dependent services (like DataStore).
 */
expect fun platformSpecificModules(): List<Module>
package com.coding.meet.newsapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.coding.meet.newsapp.di.auth.authModuleAndroid // Module Android cho Auth
// Import các module Android-specific khác của bạn nếu có (ví dụ: androidDatabaseModule)
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

// Tên cho file DataStore preferences
private const val USER_PREFERENCES_NAME = "news_app_user_settings"

// Extension property để tạo và truy cập DataStore
private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

// Module Koin chỉ để cung cấp DataStore<Preferences> trên Android
private val androidDataStoreModule = module {
    single<DataStore<Preferences>> {
        androidContext().appDataStore // Sử dụng ApplicationContext để lấy DataStore
    }
}

// Phần còn lại của databaseModule của bạn NẾU nó cần Context hoặc là Android-specific
// Ví dụ:
// val androidDatabaseModule = module {
//     single { getDatabaseBuilder(androidContext()) } // Nếu getDatabaseBuilder cần Context
//     single { getRoomDatabase(get()) } // getRoomDatabase có thể là common
//     // single { AppPreferences(get<DataStore<Preferences>>()) } // Nếu AppPreferences ở đây
// }

/**
 * Actual implementation for Android providing platform-specific Koin modules.
 */
actual fun platformSpecificModules(): List<Module> = listOfNotNull(
    authModuleAndroid,
    androidDataStoreModule
    // androidDatabaseModule, // Thêm vào nếu bạn có module này cho Android
    // Thêm các module Android-specific khác của bạn ở đây nếu có
).filterNotNull() // filterNotNull để loại bỏ các module null nếu có (mặc dù ở đây không cần thiết lắm)
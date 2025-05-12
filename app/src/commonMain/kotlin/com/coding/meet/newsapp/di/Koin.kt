package com.coding.meet.newsapp.di

import com.coding.meet.newsapp.di.auth.authModuleCommon
// Import các module common khác của bạn mà bạn đã có:
// import com.coding.meet.newsapp.di.networkModule
// import com.coding.meet.newsapp.di.databaseModule // Nếu phần lớn là common
// import com.coding.meet.newsapp.di.repositoryModule
// import com.coding.meet.newsapp.di.viewmodelModule // Cho các ViewModel không phải Auth
import com.coding.meet.newsapp.di.firebaseAuthServiceModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// Giả sử các module này đã được định nghĩa ở đâu đó trong commonMain
// Nếu không, bạn cần định nghĩa chúng hoặc comment lại.
// Ví dụ:
// val networkModule = module { /* ... */ }
// val databaseModule = module { /* ... */ } // Có thể một phần của databaseModule là platform-specific
// val repositoryModule = module { /* ... */ }
// val viewmodelModule = module { /* ... */ }


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration() // Sẽ nhận androidContext(), androidLogger() từ platform
        modules(
            // Các module common đã có của bạn
            networkModule,    // Ví dụ
            databaseModule,   // Ví dụ (phần common của nó)
            repositoryModule, // Ví dụ
            viewmodelModule,  // Ví dụ (cho các ViewModel không phải Auth)

            // Module Auth chung
            authModuleCommon,

            // Các module platform-specific được cung cấp qua expect/actual
            // Sẽ bao gồm module cung cấp AuthLocalDataSourceImpl và DataStore cho Android
            *platformSpecificModules().toTypedArray(),
            firebaseAuthServiceModule()// Module cung cấp FirebaseAuthService

        )
    }
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Required when using NativeSQLiteDriver (for Room on iOS)
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.13.0"))
            implementation(libs.firebase.auth.ktx)
            implementation("com.google.android.gms:play-services-auth:21.3.0")
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // Ktor Client for Android
            implementation(libs.ktor.client.android)
            implementation(libs.androidx.core.ktx)
            // Koin for Android
            implementation(libs.koin.android)
            // Koin integration with AndroidX Compose (Needed for koinViewModel()) - MOVED FROM commonMain
            implementation(libs.koin.androidx.compose) // Assuming composeVM maps to this or use this directly
            implementation(libs.androidx.data.store.preferences)
            // Splash API
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.lifecycle.viewmodel)
            // DataStore Preferences Implementation (Android specific) - ADDED
            implementation(libs.androidx.data.store.preferences)

            implementation(libs.androidx.foundation.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(compose.material3)

            // Navigation Compose
            implementation(libs.navigation.compose)

            // ViewModel (Moved to androidMain as it's Android specific)
            // implementation(libs.androidx.lifecycle.viewmodel) // REMOVED/MOVED

            //Coil
            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor3)

            // Room + Sqlite
            implementation(libs.androidx.room.runtime) // Runtime for common code (interfaces/entities)
            implementation(libs.sqlite.bundled) // SQLite driver bundle for KMM

            // window-size
            implementation(libs.screen.size)
            implementation(libs.androidx.lifecycle.viewmodel)
            // Ktor (Core, Serialization, Logging)
            implementation(libs.ktor.core)
            implementation(libs.ktor.json) // If using JSON serialization
            implementation(libs.ktor.logging)
            implementation(libs.ktor.negotiation) // If using content negotiation
            implementation(libs.kotlinx.serialization.json) // Kotlinx Serialization runtime

            //Kermit  for logging
            implementation(libs.kermit)

            //dataStore Core (Common interfaces)
            implementation(libs.androidx.data.store.core)

            // Koin (Core and Common Compose integration)
            api(libs.koin.core) // api because it's exposed in public APIs like ViewModels
            implementation(libs.koin.compose) // Koin integration for Compose KMM
            // Koin integration with AndroidX Compose (Needed for koinViewModel()) - MOVED TO androidMain
             implementation(libs.koin.composeVM) // REMOVED/MOVED - Assuming this maps to koin-androidx-compose
            // If libs.koin.composeVM maps to io.insert-koin:koin-compose-viewmodel
            // which is a separate KMM ViewModel artifact, keep it here.
            // PLEASE VERIFY YOUR libs.versions.toml FOR `composeVM` ALIAS.
            // Based on typical setup, koin-androidx-compose is more common for AndroidX ViewModel.
            // I'll use koin-androidx-compose in androidMain as the typical setup requires ViewModel dependency there.
            // If libs.koin.composeVM IS koin-compose-viewmodel, keep it in commonMain and ensure you use that KMM ViewModel implementation.

            // Kotlin Coroutines (Often transitive, but explicit is fine) - Optional Add
            // implementation(libs.kotlinx.coroutines.core) // If not already transitively included
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            // ktor Client for Desktop (OkHttp)
            implementation(libs.ktor.client.okhttp)
            // Coroutines for Swing (if needed for Desktop specific UI interaction)
            implementation(libs.kotlinx.coroutines.swing)
        }
        iosMain.dependencies {

            // Ktor Client for iOS (Darwin)
            implementation(libs.ktor.client.darwin)
            // Coroutines for iOS (Often transitive, but explicit is fine) - Optional Add
            // implementation(libs.kotlinx.coroutines.core) // If not already transitively included
        }
        // KSP dependencies apply to all targets that use KSP (like Room)
        dependencies {
            ksp(libs.androidx.room.compiler)
        }
    }
}

android {
    namespace = "com.coding.meet.newsapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        applicationId = "com.coding.meet.newsapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            // consider adding proguard rules for auth libraries
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        // Debug/Release Koin Android logger if needed
        // debugImplementation("io.insert-koin:koin-android-logger:x.y.z")
    }
}
dependencies {
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.compose.foundation)
}

room {
    schemaDirectory("$projectDir/schemas")
}
compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.coding.meet.newsapp"
            packageVersion = "1.0.0"
        }
    }
}
buildkonfig {
    packageName = "com.coding.meet.newsapp"

    val localProperties =
        Properties().apply {
            val propsFile = rootProject.file("local.properties")
            if (propsFile.exists()) {
                load(propsFile.inputStream())
            }
        }

    defaultConfigs {
        // Ensure API_KEY is handled securely if used for authentication backend calls
        buildConfigField(
            FieldSpec.Type.STRING,
            "API_KEY",
            localProperties["API_KEY"]?.toString() ?: "",
        )
    }
}
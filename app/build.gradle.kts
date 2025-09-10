import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "es.virtualclubs"
    compileSdk = 35

    defaultConfig {
        applicationId = "es.virtualclubs"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.1v Alpha"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    flavorDimensions += "env"

    val localProperties = Properties()
    val secretsFile = rootProject.file("secrets.properties")
    if (secretsFile.exists()) {
        localProperties.load(secretsFile.inputStream())
    }

    productFlavors {
        create("dev") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"https://192.168.3.21:8080/\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"1234567890-abc123def456.apps.googleusercontent.com\"")
        }
        create("prod") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"https://192.168.3.21:8080/\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${localProperties["GOOGLE_CLIENT_ID"]}\"")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "2.1.10" }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {

    // Dependencias de AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2")
    implementation(libs.androidx.hilt.common)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.animation.core.lint)


    // Dependencias de pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.dagger.hilt.android)
    annotationProcessor(libs.dagger.hilt.compiler)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.core)

    implementation(libs.barcode.scanning)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.security.crypto)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

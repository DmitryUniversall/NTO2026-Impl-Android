plugins {
    composeCompiler
    kotlinAndroid
    kotlinSerialization version Version.Kotlin.language
    androidApplication
}

val packageName = "ru.myitschool.work"

android {
    namespace = packageName
    compileSdk = Version.Android.Sdk.compile

    defaultConfig {
        applicationId = packageName
        minSdk = Version.Android.Sdk.min
        targetSdk = Version.Android.Sdk.target
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.viewBinding = true

    compileOptions {
        sourceCompatibility = Version.Kotlin.javaSource
        targetCompatibility = Version.Kotlin.javaSource
    }

    kotlinOptions {
        jvmTarget = Version.Kotlin.jvmTarget
    }
}

dependencies {
    defaultComposeLibrary()

    implementation("com.google.crypto.tink:tink-android:1.20.0")

    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
    implementation("androidx.navigation:navigation-compose:2.9.6")
    val coil = "3.3.0"
    implementation("io.coil-kt.coil3:coil-compose:$coil")
    implementation("io.coil-kt.coil3:coil-network-ktor3:$coil")
    val ktor = "3.3.1"
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-logging:${ktor}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}
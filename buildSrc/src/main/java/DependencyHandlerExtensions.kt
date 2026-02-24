import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependency: Dependency) {
    add(Type.IMPLEMENTATION, dependency.fullPath)
}

fun DependencyHandler.implementation(dependency: Any) {
    add(Type.IMPLEMENTATION, dependency)
}

fun DependencyHandler.testImplementation(dependency: Dependency) {
    add(Type.TEST_IMPLEMENTATION, dependency.fullPath)
}

fun DependencyHandler.androidTestImplementation(dependency: Dependency) {
    add(Type.ANDROID_TEST_IMPLEMENTATION, dependency.fullPath)
}

fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add(Type.ANDROID_TEST_IMPLEMENTATION, dependency)
}

fun DependencyHandler.api(dependency: Dependency) {
    add(Type.API, dependency.fullPath)
}

fun DependencyHandler.kapt(dependency: Dependency) {
    add(Type.KAPT, dependency.fullPath)
}

fun DependencyHandler.ksp(dependency: Dependency) {
    add(Type.KSP, dependency.fullPath)
}

fun DependencyHandler.defaultLibrary() {
    api(Dependencies.AndroidX.core)
    api(Dependencies.AndroidX.appcompat)
    api(Dependencies.AndroidX.materialDesign)
}

fun DependencyHandler.defaultComposeLibrary() {
    defaultLibrary()
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    val composeBom = platform("androidx.compose:compose-bom:2025.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
}

private object Type {
    const val IMPLEMENTATION = "implementation"
    const val TEST_IMPLEMENTATION = "testImplementation"
    const val ANDROID_TEST_IMPLEMENTATION = "androidTestImplementation"
    const val API = "api"
    const val KAPT = "kapt"
    const val KSP = "ksp"
}

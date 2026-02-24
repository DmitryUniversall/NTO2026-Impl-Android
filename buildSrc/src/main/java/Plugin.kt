import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

val PluginDependenciesSpec.androidApplication: PluginDependencySpec
    get() = id(Plugin.Id.Android.application)
val PluginDependenciesSpec.androidLibrary: PluginDependencySpec
    get() = id(Plugin.Id.Android.library)
val PluginDependenciesSpec.kotlinJvm: PluginDependencySpec
    get() = id(Plugin.Id.Kotlin.jvm)
val PluginDependenciesSpec.composeCompiler: PluginDependencySpec
    get() = id(Plugin.Id.Android.compose)
val PluginDependenciesSpec.kotlinAndroid: PluginDependencySpec
    get() = id(Plugin.Id.Kotlin.android)
val PluginDependenciesSpec.kotlinParcelize: PluginDependencySpec
    get() = id(Plugin.Id.Kotlin.parcelize)
val PluginDependenciesSpec.kotlinAnnotationProcessor: PluginDependencySpec
    get() = id(Plugin.Id.Kotlin.annotationProcessor)
val PluginDependenciesSpec.kotlinSerialization: PluginDependencySpec
    get() = kotlin(Plugin.Id.Kotlin.serialization)

object Plugin {
    object Id {
        object Android {
            /**
             * [Documentation](https://google.github.io/android-gradle-dsl/current/)
             * [Changelog](https://developer.android.com/studio/releases/gradle-plugin)
             */
            const val application = "com.android.application"
            /**
             * [Documentation](https://google.github.io/android-gradle-dsl/current/)
             * [Changelog](https://developer.android.com/studio/releases/gradle-plugin)
             */
            const val library = "com.android.library"
            /**
             * [Documentation](https://google.github.io/android-gradle-dsl/current/)
             * [Changelog](https://developer.android.com/studio/releases/gradle-plugin)
             */
            const val compose = "org.jetbrains.kotlin.plugin.compose"
        }

        object Kotlin {
            /**
             * Plugin published in https://plugins.gradle.org/
             */
            const val jvm = "org.jetbrains.kotlin.jvm"
            /**
             * Plugin published in https://plugins.gradle.org/
             */
            const val android = "org.jetbrains.kotlin.android"

            /**
             * Plugin published in https://plugins.gradle.org/
             */
            const val parcelize = "kotlin-parcelize"

            /**
             * Plugin published in https://plugins.gradle.org/
             */
            const val annotationProcessor = "kapt"

            /**
             * Plugin published in https://plugins.gradle.org/
             */
            const val serialization = "plugin.serialization"
        }
    }
}

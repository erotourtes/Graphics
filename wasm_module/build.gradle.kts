import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform") version "1.8.20"
}

group = "me.sirmax"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    wasm {
        binaries.executable()
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    open = mapOf(
                        "app" to mapOf(
                            "name" to "google-chrome", // "edge"
                            "arguments" to listOf("--js-flags=--experimental-wasm-gc")
                        )
                    ),
                )
            }
            testTask {
                useKarma {
                    this.webpackConfig.experiments.add("topLevelAwait")
//                    useChromeCanaryHeadless()
                    useChrome()
                    useConfigDirectory(project.projectDir.resolve("karma.config.d").resolve("wasm"))
                }
            }
        }
    }
    sourceSets {
//        val commonMain by getting
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test"))
//            }
//        }
        val wasmMain by getting
        val wasmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

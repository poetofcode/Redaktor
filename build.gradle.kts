plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(kotlin(module = "gradle-plugin", version = "1.7.10"))
        classpath(kotlin(module = "serialization", version = "1.7.10"))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=androidx.compose.material.ExperimentalComposeUiApi"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

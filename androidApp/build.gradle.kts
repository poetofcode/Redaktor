plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val COMPOSE_VER = "1.3.0"

android {
    namespace = "com.pragmadreams.redaktor.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.pragmadreams.redaktor.android"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = COMPOSE_VER
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:$COMPOSE_VER")
    implementation("androidx.compose.ui:ui-tooling:$COMPOSE_VER")
    implementation("androidx.compose.ui:ui-tooling-preview:$COMPOSE_VER")
    implementation("androidx.compose.foundation:foundation:$COMPOSE_VER")
    implementation("androidx.compose.material:material:$COMPOSE_VER")
    implementation("androidx.compose.material:material-icons-extended:$COMPOSE_VER")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    implementation("androidx.navigation:navigation-compose:2.5.2")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
}
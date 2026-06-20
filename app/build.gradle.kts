plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.upc.dsa.dsa_android"
    compileSdk = 36

    defaultConfig {
        applicationId = "edu.upc.dsa.dsa_android"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
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

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
    lint {
        abortOnError = false
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

dependencies {
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Unity as a Library
    implementation(project(":unityLibrary"))
}
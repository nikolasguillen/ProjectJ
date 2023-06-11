@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id(libs.plugins.kotlinKapt.get().pluginId)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.example.projectj"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.projectj"
        minSdk = 26
        targetSdk = 33
        compileSdkPreview = "UpsideDownCake"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += ("META-INF/DEPENDENCIES")
            excludes += ("META-INF/LICENSE")
            excludes += ("META-INF/LICENSE.txt")
            excludes += ("META-INF/license.txt")
            excludes += ("META-INF/NOTICE")
            excludes += ("META-INF/NOTICE.txt")
            excludes += ("META-INF/notice.txt")
            excludes += ("META-INF/ASL2.0")
            excludes += ("META-INF/*.kotlin_module")
            excludes += ("META-INF/gradle/incremental.annotation.processors")
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.material3.windowsize)
    implementation(libs.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.coil.compose)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.daggerHilt)
    implementation(libs.daggerHilt.compiler)

    implementation(libs.navigation)
    implementation(libs.retrofit)

    implementation(libs.accompanist.adaptive)

}
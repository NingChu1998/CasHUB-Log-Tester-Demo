plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // Required to satisfy the latest AndroidX libraries
    // Stick to 34 to match your Android Studio capabilities
    namespace = "com.example.cashub_demo"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.cashub_demo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // DOWNGRADED VERSIONS to support AGP 8.1.4
    implementation("androidx.core:core-ktx:1.13.1")        // Instead of 1.16.0
    implementation("androidx.appcompat:appcompat:1.6.1")    // Stable for SDK 34
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.8.2")  // Instead of 1.12.3

    // WorkManager for Scenario 3
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
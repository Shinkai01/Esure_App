
plugins {
    id("com.google.gms.google-services")
    id("com.android.application")
}

android {
    namespace = "com.example.esureapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.esureapp"
        minSdk = 26
        targetSdk = 34
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
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth:21.0.0")
    implementation("com.google.firebase:firebase-dynamic-module-support:16.0.0-beta03")
}


dependencies {

    implementation ("com.google.firebase:firebase-messaging:24.0.2")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-database")
    implementation ("com.google.android.exoplayer:exoplayer:2.16.1")
    implementation ("com.google.android.gms:play-services-maps:18.0.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
}
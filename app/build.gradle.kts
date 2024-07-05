plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "seifemadhamdy.supplera"
  compileSdk = 34

  defaultConfig {
    applicationId = "seifemadhamdy.supplera"
    minSdk = 21
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
    vectorDrawables { useSupportLibrary = true }
  }

  buildTypes {
    release {
      isDebuggable = false
      isCrunchPngs = true
      isMinifyEnabled = false
      isShrinkResources = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    isCoreLibraryDesugaringEnabled = true
  }

  kotlinOptions { jvmTarget = "21" }

  buildFeatures {
    compose = true
    viewBinding = true
  }

  composeOptions { kotlinCompilerExtensionVersion = "1.5.11" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
  implementation("androidx.activity:activity-compose:1.9.0")
  implementation(platform("androidx.compose:compose-bom:2024.05.00"))
  implementation("androidx.compose.ui:ui:1.6.7")
  implementation("androidx.compose.ui:ui-graphics:1.6.7")
  implementation("androidx.compose.material3:material3:1.2.1")
  implementation("androidx.core:core-splashscreen:1.0.1")
  implementation("dev.chrisbanes.haze:haze:0.6.2")
  implementation(platform("androidx.compose:compose-bom:2024.05.00"))
  implementation("io.coil-kt:coil-compose:2.6.0")
  implementation("androidx.compose.material:material-icons-extended:1.6.7")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
  implementation("androidx.navigation:navigation-compose:2.7.7")
  implementation("androidx.palette:palette-ktx:1.0.0")
  implementation("androidx.recyclerview:recyclerview:1.3.2")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.compose.ui:ui-viewbinding:1.6.7")
  implementation("net.engawapg.lib:zoomable:1.6.1")
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
  implementation("androidx.datastore:datastore-preferences:1.1.1")
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
  androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
  debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")
}

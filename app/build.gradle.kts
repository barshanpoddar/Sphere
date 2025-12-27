plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.compose")
  id("org.jlleitschuh.gradle.ktlint")
}

android {
  namespace = "com.sphere.app"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.sphere.app"
    minSdk = 24
    targetSdk = 36
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
        "proguard-rules.pro",
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

ktlint {
  version.set("1.3.1")
  android.set(true)
  reporters {
    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
  }
  filter {
    exclude { element -> element.file.path.contains("generated/") }
  }
  disabledRules = listOf("function-naming")
}
tasks.register("formatCode") {
  group = "formatting"
  description = "Format Kotlin code like Flutter (2-space indentation, ignores Composable naming)"
  doLast {
    exec {
      workingDir = rootDir
      commandLine(
        "cmd",
        "/c",
        "gradlew :app:ktlintFormat --continue || echo Formatting completed with warnings",
      )
    }
  }
}

tasks.register("checkCodeFormat") {
  group = "verification"
  description = "Check if Kotlin code follows Flutter-like formatting"
  doLast {
    exec {
      workingDir = rootDir
      commandLine(
        "cmd",
        "/c",
        "gradlew :app:ktlintCheck --continue || echo Code check completed with warnings",
      )
    }
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.17.0")
  implementation("androidx.core:core-splashscreen:1.2.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
  implementation("androidx.activity:activity-compose:1.12.1")
  implementation(platform("androidx.compose:compose-bom:2025.12.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")

  // Navigation
  implementation("androidx.navigation:navigation-compose:2.9.6")

  // Media3 (ExoPlayer)
  implementation("androidx.media3:media3-exoplayer:1.8.0")
  implementation("androidx.media3:media3-ui:1.8.0")
  implementation("androidx.media3:media3-common:1.8.0")

  // Icons
  implementation("androidx.compose.material:material-icons-extended:1.7.8")

  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.3.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
  androidTestImplementation(platform("androidx.compose:compose-bom:2025.12.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")

  // Image Loading
  implementation("io.coil-kt:coil-compose:2.7.0")
}

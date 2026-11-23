import java.util.Properties
import java.io.FileInputStream

// Cargar credenciales del keystore para firmar el APK
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

// Cargar API key de Google Books
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.28"
}

android {
    namespace = "com.example.mpaz_neb"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        buildConfigField("String", "GOOGLE_BOOKS_API_KEY", "\"${localProperties.getProperty("GOOGLE_BOOKS_API_KEY") ?: ""}\"")
        applicationId = "com.example.mpaz_neb"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 👇 NUEVO — Configuración de firma
    signingConfigs {
        create("release") {
            storeFile = file("../apk/mpaz-neb-release.jks")
            storePassword = keystoreProperties.getProperty("storePassword") ?: ""
            keyAlias = keystoreProperties.getProperty("keyAlias") ?: ""
            keyPassword = keystoreProperties.getProperty("keyPassword") ?: ""
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 👇 NUEVO — Usar la firma en el build release
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt:coil-compose:2.6.0")
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // === Testing con Kotest + JUnit5 + MockK (Guía 15) ===
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
}
    // JaCoCo - Cobertura de código (para pauta EP4)
    apply(plugin = "jacoco")

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*")
        }
    }

    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        val fileFilter = listOf(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*",
            // === Excluir generados ===
            "**/*_Impl*.*", "**/*Dao*.*", "**/*Database*.*",
            // === Excluir DTOs / sin lógica ===
            "**/data/model/**",
            "**/data/remote/**",
            "**/model/**",
            "**/ui/**",
            "**/navigation/**",
            "**/theme/**",
            "**/di/**",
            "**/Application*.*",
            "**/MainActivity*.*",
            "**/sesion/SesionManager*.*",
            // === Excluir demos/experimentales del inicio del proyecto ===
            "**/database/**",                    // Room (UsuarioEntity, UsuarioDao, AppDatabase)
            "**/PostViewModel*.*",                // Demo Retrofit (ya cumplió su función)
            "**/BookViewModel*.*",                // API externa (Google Books)
            "**/BookRepository*.*",               // API externa (Google Books)
            "**/viewmodels/UsuarioViewModel*.*"   // Registro legacy (Room local)
        )

        // 👇 ESTO ES LO QUE FALTABA
        val debugTree = fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(fileFilter)
        }
        val mainSrc = "${project.projectDir}/src/main/java"

        sourceDirectories.setFrom(files(mainSrc))
        classDirectories.setFrom(files(debugTree))
        executionData.setFrom(
            fileTree("${project.layout.buildDirectory.get()}") {
                include("**/*.exec", "**/*.ec")
            }
        )
    }
}
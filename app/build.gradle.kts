import com.android.build.gradle.internal.utils.restrictRenderScriptOnRiscv
import java.io.FileInputStream
import java.util.Properties
import com.google.gson.Gson
import org.gradle.api.file.Directory
import java.io.File

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

// Define version variables at a broader scope
var _versionCode = 0
var _major = 0
var _minor = 0
var _patch = 0

val versionPropsFile = file("version.properties")

if (versionPropsFile.canRead()) {
    val versionProps = Properties().apply {
        load(FileInputStream(versionPropsFile))
    }

    _patch = versionProps["PATCH"].toString().toInt() + 1
    _major = versionProps["MAJOR"].toString().toInt()
    _minor = versionProps["MINOR"].toString().toInt()
    _versionCode = versionProps["VERSION_CODE"].toString().toInt() + 1

    if (_patch == 100) {
        _patch = 0
        _minor += 1
    }
    if (_minor == 10) {
        _minor = 0
        _major += 1
    }

    versionProps["MAJOR"] = _major.toString()
    versionProps["MINOR"] = _minor.toString()
    versionProps["PATCH"] = _patch.toString()
    versionProps["VERSION_CODE"] = _versionCode.toString()
    versionProps.store(versionPropsFile.writer(), null)
} else {
    throw GradleException("Could not read version.properties!")
}

val _versionName = "$_major.$_minor.$_patch:$_versionCode"

android {
    packaging{
        resources{
            excludes += listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.txt"
            )
        }
    }
    lint {
        checkReleaseBuilds = false
    }
    namespace = "com.kitaotao.sst"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kitaotao.sst"
        minSdk = 24
        targetSdk = 34
        versionCode = _versionCode
        versionName = _versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "VERSION_NAME", "\"$_versionName\"")
        buildConfigField("int", "VERSION_CODE", _versionCode.toString())
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

// Define the version JSON structure
data class VersionJson(
    val versionCode: Int,
    val versionName: String,
    val releaseUrl: String
)

// Use layout.buildDirectory to access the build directory and set the path for version.json
val releaseDirPath = File(projectDir, "appVersion")

tasks.register("generateVersionJson") {
    doLast {
        // Ensure the release directory exists
        if (!releaseDirPath.exists()) {
            releaseDirPath.mkdirs()
        }

        // Define the version.json file path
        val versionJsonFile = File(releaseDirPath, "version.json")

        val versionJson = VersionJson(
            versionCode = _versionCode,
            versionName = "$_major.$_minor.$_patch",
            releaseUrl = "https://github.com/yourusername/yourrepository/releases/latest"
        )

        versionJsonFile.writeText(Gson().toJson(versionJson))
        println("Generated version.json at ${versionJsonFile.path}")
    }
}

// Attach to build task
tasks.named("build") {
    dependsOn("generateVersionJson")
}

// Optionally attach to assembleRelease if it exists
tasks.matching { it.name == "assembleRelease" }.configureEach {
    dependsOn("generateVersionJson")
}

allprojects {
    repositories {

    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    // More dependencies...
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("com.airbnb.android:lottie:6.5.2")
    implementation("io.github.sgpublic:MultiWaveHeader:1.0.2")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("org.osmdroid:osmdroid-android:6.1.10")
    implementation("com.graphhopper:graphhopper-core:6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")

}

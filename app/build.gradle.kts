import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")

}

android {
    lint {
        checkReleaseBuilds = false
    }
    namespace = "com.kitaotao.sst"
    compileSdk = 34

    // start

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
        _versionCode = versionProps["VERSION_CODE"].toString().toInt() + 3

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

allprojects{
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
    //implementation(libs.androidx.preference)
    //implementation(libs.androidx.gridlayout)
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
}
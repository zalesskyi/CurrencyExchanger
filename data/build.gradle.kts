plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}
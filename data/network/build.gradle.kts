plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("plugin.serialization") version "1.9.22"

    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.squareup.retrofit)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.serialization.converter)
}
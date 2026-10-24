plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.gitlab.arturbosch.detekt")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.coroutines.core)

    testImplementation(libs.junit)
}

plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        optIn.add("io.github.ardiien.datasize.ExperimentalDataSizeApi")
    }
}

dependencies {
    implementation(projects.datasizeCore)
    testImplementation(libs.kotlin.test)
}
plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        optIn.add("team.idivision.libs.datasize.ExperimentalDataSizeApi")
    }
}

dependencies {
    implementation(projects.core)

    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}
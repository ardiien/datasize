plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(17)

    jvm()

    compilerOptions {
        optIn.add("io.github.ardiien.datasize.ExperimentalDataSizeApi")
    }
}

//dependencies {
//    implementation(projects.datasizeCore)
//    testImplementation(libs.kotlin.test)
//}
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(17)
    jvm()

    compilerOptions {
        optIn.add("io.github.ardiien.datasize.ExperimentalDataSizeApi")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.datasizeCore)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

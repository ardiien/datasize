plugins {
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.bcv)
}

group = "io.github.ardiien.datasize"
version = "1.0.0"

kotlin {
    jvmToolchain(17)

    explicitApi()
    compilerOptions {
        optIn.add("io.github.ardiien.datasize.ExperimentalDataSizeApi")
    }

    sourceSets.all {
        kotlin.srcDirs("$name/src")
        resources.srcDirs("$name/resources")
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifactId = "datasize"
        }
    }
}
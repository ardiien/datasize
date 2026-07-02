import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.bcv)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.mcentral.publish)
}

group = "io.github.ardiien.datasize"
version = "2.1.0"

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(17)

    explicitApi()
    compilerOptions {
        optIn.add("io.github.ardiien.datasize.ExperimentalDataSizeApi")
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.collections)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dokka {
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/$name/kotlin"))
            remoteUrl("https://github.com/ardiien/datasize/tree/main/datasize-core/src/$name/kotlin")
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    configureBasedOnAppliedPlugins(
        javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
        sourcesJar = SourcesJar.Sources()
    )

    coordinates(
        groupId = group.toString(),
        artifactId = "datasize",
        version = version.toString(),
    )

    pom {
        name.set("datasize")
        description.set("A lightweight Kotlin library for representing and operating on data sizes")
        inceptionYear.set("2026")
        url.set("https://github.com/ardiien/datasize")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("yovdiienko")
                name.set("Yaroslav Ovdiienko")
                email.set("idivision.developer@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:https://github.com/ardiien/datasize.git")
            developerConnection.set("scm:git:ssh://github.com:ardiien/datasize.git")
            url.set("https://github.com/ardiien/datasize")
        }
    }
}
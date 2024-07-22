plugins {
    java
    application
    alias(libs.plugins.style)
    alias(libs.plugins.jagr.gradle)
}

version = file("version").readLines().first()

jagr {
    assignmentId.set("h03")
    submissions {
        val main by creating {
            // studentId.set("")
            // firstName.set("")
            // lastName.set("")
        }
    }
    graders {
        val graderPrivate by creating {
            graderName.set("H03-Private")
            rubricProviderName.set("h03.H03_RubricProvider")
            configureDependencies{
                implementation(libs.algoutils.tutor)
            }
        }
    }
}

dependencies {
    implementation(libs.annotations)
    implementation(libs.algoutils.student)
    implementation(libs.fopbot)
    testImplementation(libs.junit.core)
}

application {
    mainClass.set("h03.Main")
}

tasks {
    val runDir = File("build/run")
    withType<JavaExec> {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
    test {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

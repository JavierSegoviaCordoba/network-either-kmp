plugins {
    id("de.marcphilipp.nexus-publish")
    signing
    id("Dokka")
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    dependsOn(tasks.dokkaJavadoc)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set(POM.name)
            description.set(POM.description)
            url.set(POM.url)
            licenses {
                license {
                    name.set(POM.License.name)
                    url.set(POM.License.url)
                }
            }
            developers {
                developer {
                    id.set(POM.Developer.id)
                    name.set(POM.Developer.name)
                    email.set(POM.Developer.email)
                }
            }
            scm {
                url.set(POM.SMC.url)
                connection.set(POM.SMC.connection)
                developerConnection.set(POM.SMC.developerConnection)
            }
        }
    }

    publications {
        publications.configureEach {
            if (this is MavenPublication) {
                artifact(dokkaJar)
            }
        }
    }
}

nexusPublishing {
    this.repositories {
        sonatype()
    }

    useStaging.set(isLibRelease)
}

signing {
    if (isLibRelease) {
        useGpgCmd()
        sign(publishing.publications)
    }
}

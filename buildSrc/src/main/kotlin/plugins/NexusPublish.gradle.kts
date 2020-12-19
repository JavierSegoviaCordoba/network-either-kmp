plugins {
    id("de.marcphilipp.nexus-publish")
    signing
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Logger")
            description.set("Logger Multiplatform")
            url.set("http://github.com/JavierSegoviaCordoba/logger")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("JavierSegoviaCordoba")
                    name.set("Javier Segovia Cordoba")
                    email.set("javiersegoviacordoba@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/JavierSegoviaCordoba/logger")
                connection.set("scm:git:https://github.com/JavierSegoviaCordoba/logger.git")
                developerConnection.set("scm:git:git@github.com:JavierSegoviaCordoba/logger.git")
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

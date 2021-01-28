object POM {

    const val name = "Either"
    const val description = "Either Multiplatform"
    const val url = "http://github.com/JavierSegoviaCordoba/either"

    object License {
        const val name = "The Apache License, Version 2.0"
        const val url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    }

    object Developer {
        const val id = "JavierSegoviaCordoba"
        const val name = "Javier Segovia Cordoba"
        const val email = "javier@segoviacordoba.com"
    }

    object SMC {
        private const val userRepo = "JavierSegoviaCordoba/either"

        const val url = "https://github.com/$userRepo"
        const val connection = "scm:git:$url.git"
        const val developerConnection = "scm:git:git@github.com:$userRepo.git"
    }
}

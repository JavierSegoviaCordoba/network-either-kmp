![Kotlin version](https://img.shields.io/badge/kotlin-2.0.21-blueviolet?logo=kotlin&logoColor=white)
[![MavenCentral](https://img.shields.io/maven-central/v/com.javiersc.network/network-either?label=MavenCentral)](https://repo1.maven.org/maven2/com/javiersc/network/network-either/)
[![Snapshot](https://img.shields.io/nexus/s/com.javiersc.network/network-either?server=https%3A%2F%2Foss.sonatype.org%2F&label=Snapshot)](https://oss.sonatype.org/content/repositories/snapshots/com/javiersc/network/network-either/)

[![Build](https://img.shields.io/github/actions/workflow/status/JavierSegoviaCordoba/network-either-kmp/build-kotlin.yaml?label=Build&logo=GitHub)](https://github.com/JavierSegoviaCordoba/network-either-kmp/tree/main)
[![Coverage](https://img.shields.io/sonar/coverage/com.javiersc.network:network-either?label=Coverage&logo=SonarCloud&logoColor=white&server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=com.javiersc.network:network-either)
[![Quality](https://img.shields.io/sonar/quality_gate/com.javiersc.network:network-either?label=Quality&logo=SonarCloud&logoColor=white&server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=com.javiersc.network:network-either)
[![Tech debt](https://img.shields.io/sonar/tech_debt/com.javiersc.network:network-either?label=Tech%20debt&logo=SonarCloud&logoColor=white&server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=com.javiersc.network:network-either)

# Network Either

## Ktor

### Usage

```kotlin
val client: HttpClient = HttpClient(MockEngine) {
    install(NetworkEitherPlugin)
    install(ContentNegotiation) {
        json(defaultJson)
    }
}

@Serializable
data class DogDTO(val id: Int, val name: String, val age: Int)

@Serializable
data class ErrorDTO(val message: String)

val response: NetworkEither<ErrorDTO, DogDTO> = client.get("dog").body()
response.fold(
    failure = { ... },
    success = { ... },
)
```

Check all available functions in the [
`NetworkEither` class](subprojects/network-either/common/main/kotlin/com/javiersc/network/either/NetworkEither.kt).

Check [the tests](subprojects/network-either/common/test/kotlin/com/javiersc/network/either/KtorTest.kt)
for more examples.

## Retrofit

### Usage

```kotlin
val retrofit =
    Retrofit
        .Builder()
        .baseUrl("org.example.com")
        .addCallAdapterFactory(NetworkEitherCallAdapterFactory())
        .build()

interface DogService {

    @GET("dog")
    suspend fun getDog(): NetworkEither<ErrorDTO, DogDTO>
}

val service: DogService = retrofit.create()
val response: NetworkEither<ErrorDTO, DogDTO> = service.getDog()
response.fold(
    failure = { ... },
    success = { ... },
)
```

Check [the tests](subprojects/network-either/jvm/test/kotlin/com/javiersc/network/either/RetrofitTest.kt)
for more examples.

## Docs

All docs are available on the [`network-either-kmp` website](https://network-either-kmp.javiersc.com)

## Download from MavenCentral

- NetworkEither, pretty printing logger and ResourceEither mappers

```kotlin
implementation("com.javiersc.network:network-either:$version")
```

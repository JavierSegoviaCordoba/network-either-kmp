# Changelog

## [Unreleased]

### Added

- `network-either-arrow` project with Arrow's `Either` integration
- `ios` support
- `macos` support
- `watchos` support
- `tvos` support
- `nodejs` support
- `mingw` support
- `WAsm > JS > browser` support
- `WAsm > JS > nodejs` support

### Changed

- `isNetworkAvailable` function to be `suspend`

### Deprecated

### Fixed

### Removed

- Arrow `toEither` function in the main project

### Updated

- `com.javiersc.hubdle:com.javiersc.hubdle.gradle.plugin -> 0.14.0`
- `gradle -> 8.14.3`

## [0.3.0-alpha.1] - 2024-10-26

### Updated

- `com.javiersc.hubdle:com.javiersc.hubdle.gradle.plugin -> 0.7.18`
- `gradle -> 8.10.2`

## [0.2.0-alpha.2] - 2023-02-20

### Fixed

- crashing when getting `Network.Local`

### Updated

- `com.javiersc.hubdle:com.javiersc.hubdle.gradle.plugin -> 0.4.0-alpha.11`
- `gradle -> 8.0`

## [0.2.0-alpha.1] - 2022-07-26

### Added

- `NetworkEitherPlugin` ktor plugin

### Changed

- project name `network-core` to `network-either`
- project name `network-logger` to `network-either-logger`
- project name `network-resource` to `network-resource-either-extensions`
- project name `resource-core` to `resource-either`
- all projects  group to `com.javiersc.network`
- `ResourceEither` is its own `sealed class`
- `ResourceEither` package to `com.javiersc.resource.either`
- `ResourceEither` uses Arrow's `Either`
- `NetworkEither` is its own `sealed class`
- `NetworkEither` package to `com.javiersc.network.either`
- `NetworkEither` uses Arrow's `Either`
- `alsoPrettyPrint` function to `alsoLog`

### Removed

- `either-core` artifacts (Arrow's `Either` is already migrated to Kotlin Multiplatform)
- old `NetworkEitherKtor` implementation

### Updated

- `gradle -> 7.5`

## [0.1.0-alpha.3] - 2021-07-19

### Changed

- `NetworkEitherCore` use Ktor `HttpClient`

## [0.1.0-alpha.2] - 2021-07-16

- No changes

## [0.1.0-alpha.1] - 2021-06-23

- No changes

## [0.0.0] - 2021-01-01

- No changes

[Unreleased]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.3.0-alpha.1...HEAD

[0.3.0-alpha.1]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.2.0-alpha.2...0.3.0-alpha.1

[0.2.0-alpha.2]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.2.0-alpha.1...0.2.0-alpha.2

[0.2.0-alpha.1]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.1.0-alpha.3...0.2.0-alpha.1

[0.1.0-alpha.3]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.1.0-alpha.2...0.1.0-alpha.3

[0.1.0-alpha.2]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.1.0-alpha.1...0.1.0-alpha.2

[0.1.0-alpha.1]: https://github.com/JavierSegoviaCordoba/network-either-kmp/compare/0.0.0...0.1.0-alpha.1

[0.0.0]: https://github.com/JavierSegoviaCordoba/network-either-kmp/commits/0.0.0

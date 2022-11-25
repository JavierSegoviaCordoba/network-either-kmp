# Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Updated

- `gradle -> 7.6`
- `com.javiersc.hubdle:com.javiersc.hubdle.gradle.plugin -> 0.2.0-alpha.44`

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

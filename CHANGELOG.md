# Changelog

## [Unreleased]

### Added

- `NetworkEitherPlugin` ktor plugin

### Changed

- `NetworkEither` is its own `sealed class`
- `NetworkEither` package to `com.javiersc.network.either`
- `NetworkEither` uses Arrow's `Either`
- `alsoPrettyPrint` function to `alsoLog`

### Deprecated

### Removed

- `either-core` artifacts (Arrow's `Either` is already migrated to Kotlin Multiplatform)
- old `NetworkEitherKtor` implementation

### Fixed

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

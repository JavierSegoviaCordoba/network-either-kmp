# detekt

## Metrics

* 461 number of properties

* 323 number of functions

* 137 number of classes

* 27 number of packages

* 171 number of kt files

## Complexity Report

* 5,782 lines of code (loc)

* 4,592 source lines of code (sloc)

* 2,598 logical lines of code (lloc)

* 137 comment lines of code (cloc)

* 558 cyclomatic complexity (mcc)

* 151 cognitive complexity

* 94 number of total code smells

* 2% comment source ratio

* 214 mcc per 1,000 lloc

* 36 code smells per 1,000 lloc

## Findings (94)

### exceptions, SwallowedException (1)

The caught exception is swallowed. The original exception could be lost.

[Documentation](https://detekt.dev/docs/rules/exceptions#swallowedexception)

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:10:18
```
The caught exception is swallowed. The original exception could be lost.
```
```kotlin
7      scope.sendPipeline.intercept(HttpSendPipeline.Before) {
8          try {
9              proceed()
10         } catch (throwable: Throwable) {
!!                  ^ error
11             val outgoingContent = RemoteErrorOutgoing
12             proceedWith(scope.fakeFailureHttpClientCall(outgoingContent))
13         }

```

### exceptions, TooGenericExceptionCaught (2)

The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptioncaught)

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptBeforeTransformAndReplaceContainerWithNetowrkEitherType.kt:33:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
30 
31         try {
32             proceedWith(modifiedContainer)
33         } catch (throwable: Throwable) {
!!                  ^ error
34             val typeInfo = typeInfo<Either.Left<Nothing>>()
35             val networkEither: NetworkEither<Any, Nothing> = buildNetworkFailureUnknown(throwable)
36             proceedWith(HttpResponseContainer(typeInfo, networkEither))

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:10:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
7      scope.sendPipeline.intercept(HttpSendPipeline.Before) {
8          try {
9              proceed()
10         } catch (throwable: Throwable) {
!!                  ^ error
11             val outgoingContent = RemoteErrorOutgoing
12             proceedWith(scope.fakeFailureHttpClientCall(outgoingContent))
13         }

```

### naming, InvalidPackageDeclaration (71)

Kotlin source files should be stored in the directory corresponding to its package statement.

[Documentation](https://detekt.dev/docs/rules/naming#invalidpackagedeclaration)

* subprojects/network/network-core/jvm/main/kotlin/NetworkEitherCallAdapterFactory.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network
! ^ error
2 
3 import com.javiersc.either.Either
4 import com.javiersc.either.network.internal.deferred.NetworkEitherDeferredCallAdapter

```

* subprojects/network/network-core/jvm/main/kotlin/internal/deferred/HandleDeferred.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.deferred
! ^ error
2 
3 import com.javiersc.either.network.Headers
4 import com.javiersc.either.network.HttpStatusCode

```

* subprojects/network/network-core/jvm/main/kotlin/internal/deferred/HttpExceptionDeferredHandler.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.deferred
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.internal.utils.headers

```

* subprojects/network/network-core/jvm/main/kotlin/internal/deferred/NetworkEitherDeferredCall.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.deferred
! ^ error
2 
3 import com.javiersc.either.network.HttpStatusCode as NetworkHttpStatusCode
4 import com.javiersc.either.network.NetworkEither

```

* subprojects/network/network-core/jvm/main/kotlin/internal/deferred/NetworkEitherDeferredCallAdapter.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.deferred
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import java.lang.reflect.Type

```

* subprojects/network/network-core/jvm/main/kotlin/internal/deferred/ResponseDeferredHandler.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.deferred
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.internal.utils.headers

```

* subprojects/network/network-core/jvm/main/kotlin/internal/suspend/HandleSuspend.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.suspend
! ^ error
2 
3 import com.javiersc.either.network.Headers
4 import com.javiersc.either.network.HttpStatusCode

```

* subprojects/network/network-core/jvm/main/kotlin/internal/suspend/HttpExceptionSuspendHandler.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.suspend
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.internal.utils.headers

```

* subprojects/network/network-core/jvm/main/kotlin/internal/suspend/NetworkEitherSuspendCall.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.suspend
! ^ error
2 
3 import com.javiersc.either.network.HttpStatusCode as NetworkHttpStatusCode
4 import com.javiersc.either.network.NetworkEither

```

* subprojects/network/network-core/jvm/main/kotlin/internal/suspend/NetworkEitherSuspendCallAdapter.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.suspend
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import java.lang.reflect.Type

```

* subprojects/network/network-core/jvm/main/kotlin/internal/suspend/ResponseSuspendHandler.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.suspend
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.internal.utils.headers

```

* subprojects/network/network-core/jvm/main/kotlin/internal/utils/HttpStatusCodeUtils.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.utils
! ^ error
2 
3 import io.ktor.http.Headers
4 import io.ktor.http.HttpStatusCode

```

* subprojects/network/network-core/jvm/main/kotlin/internal/utils/Printers.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.utils
! ^ error
2 
3 private const val BRIGHT_YELLOW = "\u001B[93m"
4 private const val BRIGHT_RED = "\u001B[91m"

```

* subprojects/network/network-core/jvm/main/kotlin/internal/utils/RetrofitUtils.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.internal.utils
! ^ error
2 
3 import io.ktor.http.Headers
4 import io.ktor.http.HttpStatusCode

```

* subprojects/network/network-core/jvm/test/kotlin/TestUtils.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network
! ^ error
2 
3 import okio.FileSystem
4 

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/BaseTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit
! ^ error
2 
3 import com.javiersc.either.network.Headers
4 import com.javiersc.either.network.HttpStatusCode

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error400Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error401Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error402Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error403Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error404Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error405Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error406Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error409Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error410Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error411Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error412Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error413Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error414Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error415Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error416Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error417Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error418Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error421Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error422Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error423Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error424Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error426Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error428Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error429Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error431Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error451Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/clientError/Error4xxTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.clientError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/config/DogService.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.config
! ^ error
2 
3 import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
4 import com.javiersc.either.network.NetworkEither

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/config/MockResponseUtil.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.config
! ^ error
2 
3 import com.javiersc.either.network.Headers
4 import com.javiersc.either.network.readResource

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/other/LocalErrorTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.other
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureLocal

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/other/MalformedJsonTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.other
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.NetworkFailureUnknown

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/other/RemoteErrorTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.other
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureRemote

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error500Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error501Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error502Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error503Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error504Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error505Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error506Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error507Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error508Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error509Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error510Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error511Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/serverError/Error5xxTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.serverError
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkFailureHttp

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success200Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success201Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success202Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success203Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success204Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success205Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success206Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success207Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success226Test.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/success/Success2xxTest.kt:1:1
```
The package declaration does not match the actual file location.
```
```kotlin
1 package com.javiersc.either.network.retrofit.success
! ^ error
2 
3 import com.javiersc.either.network.NetworkEither
4 import com.javiersc.either.network.buildNetworkSuccess

```

### naming, PackageNaming (7)

Package names should match the naming convention set in the configuration.

[Documentation](https://detekt.dev/docs/rules/naming#packagenaming)

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/HttpStatusCodeExtensions.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import io.ktor.http.Headers
4 import io.ktor.http.HttpStatusCode

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/NetworkEitherFailureOutgoings.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import io.ktor.http.ContentType
4 import io.ktor.http.Headers

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/PipelineContextExtensions.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import com.javiersc.either.Either
4 import com.javiersc.either.network.NetworkEither

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/fakeFailureHttpClientCall.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import io.ktor.client.HttpClient
4 import io.ktor.client.call.HttpClientCall

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptBeforeTransformAndReplaceContainerWithNetowrkEitherType.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import com.javiersc.either.Either
4 import com.javiersc.either.network.NetworkEither

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import io.ktor.client.HttpClient
4 import io.ktor.client.request.HttpSendPipeline

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptSuccessesAndReplaceWithNetworkEitherSuccess.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.either.network.ktor._internal
! ^ error
2 
3 import com.javiersc.either.network.HttpStatusCode as NetworkEitherHttpStatusCode
4 import com.javiersc.either.network.NetworkEither

```

### naming, TopLevelPropertyNaming (2)

Top level property names should follow the naming convention set in the projects configuration.

[Documentation](https://detekt.dev/docs/rules/naming#toplevelpropertynaming)

* subprojects/network/network-core/common/test/kotlin/com/javiersc/either/network/ktor/other/MalformedJsonTest.kt:37:19
```
Top level constant names should match the pattern: [A-Z][_A-Z0-9]*
```
```kotlin
34     }
35 }
36 
37 private const val partialMessage = "Unexpected JSON token at offset"
!!                   ^ error
38 

```

* subprojects/network/network-core/jvm/test/kotlin/retrofit/other/MalformedJsonTest.kt:49:19
```
Top level constant names should match the pattern: [A-Z][_A-Z0-9]*
```
```kotlin
46     }
47 }
48 
49 private const val partialMessage = "Unexpected JSON token at offset"
!!                   ^ error
50 

```

### performance, SpreadOperator (2)

In most cases using a spread operator causes a full copy of the array to be created before calling a method. This may result in a performance penalty.

[Documentation](https://detekt.dev/docs/rules/performance#spreadoperator)

* subprojects/network/network-core/jvm/main/kotlin/internal/utils/RetrofitUtils.kt:16:22
```
In most cases using a spread operator causes a full copy of the array to be created before calling a method. This may result in a performance penalty.
```
```kotlin
13     get() = code() toHttpStatusCode message()
14 
15 internal val HttpException.headers: Headers
16     get() = headersOf(*response()?.headers().toOkHttpHeaders())
!!                      ^ error
17 
18 internal val Response<*>.headers: Headers
19     get() = headersOf(*headers().toOkHttpHeaders())

```

* subprojects/network/network-core/jvm/main/kotlin/internal/utils/RetrofitUtils.kt:19:22
```
In most cases using a spread operator causes a full copy of the array to be created before calling a method. This may result in a performance penalty.
```
```kotlin
16     get() = headersOf(*response()?.headers().toOkHttpHeaders())
17 
18 internal val Response<*>.headers: Headers
19     get() = headersOf(*headers().toOkHttpHeaders())
!!                      ^ error
20 
21 private fun okhttp3.Headers?.toOkHttpHeaders(): Array<Pair<String, List<String>>> {
22     return this?.toMultimap()?.map { header -> header.key to header.value }?.toTypedArray()

```

### style, MagicNumber (8)

Report magic numbers. Magic number is a numeric literal that is not defined as a constant and hence it's unclear what the purpose of this number is. It's better to declare such numbers as constants and give them a proper name. By default, -1, 0, 1, and 2 are not considered to be magic numbers.

[Documentation](https://detekt.dev/docs/rules/style#magicnumber)

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/NetworkEitherFailureOutgoings.kt:18:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
15 
16     override fun toString(): String = key
17 
18     override val status: HttpStatusCode = HttpStatusCode(9345, key)
!!                                                          ^ error
19 
20     override val headers: Headers =
21         headersOf(networkEitherHeader("Failure.Remote"), jsonContentTypeHeader)

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/NetworkEitherFailureOutgoings.kt:32:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
29 
30     override fun toString(): String = key
31 
32     override val status: HttpStatusCode = HttpStatusCode(9345, key)
!!                                                          ^ error
33 
34     override val headers: Headers =
35         headersOf(networkEitherHeader("Failure.Remote"), jsonContentTypeHeader)

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/PipelineContextExtensions.kt:110:39
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
107 internal val PipelineContext<HttpResponseContainer, HttpClientCall>.httpResponse: HttpResponse
108     get() = context.response
109 
110 internal val successRange: IntRange = 200..299
!!!                                       ^ error
111 internal val errorRange: IntRange = 400..599
112 

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/PipelineContextExtensions.kt:110:44
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
107 internal val PipelineContext<HttpResponseContainer, HttpClientCall>.httpResponse: HttpResponse
108     get() = context.response
109 
110 internal val successRange: IntRange = 200..299
!!!                                            ^ error
111 internal val errorRange: IntRange = 400..599
112 

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/PipelineContextExtensions.kt:111:37
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
108     get() = context.response
109 
110 internal val successRange: IntRange = 200..299
111 internal val errorRange: IntRange = 400..599
!!!                                     ^ error
112 

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/PipelineContextExtensions.kt:111:42
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
108     get() = context.response
109 
110 internal val successRange: IntRange = 200..299
111 internal val errorRange: IntRange = 400..599
!!!                                          ^ error
112 

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptSuccessesAndReplaceWithNetworkEitherSuccess.kt:20:16
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
17         val response = subject.response
18         val code = NetworkEitherHttpStatusCode(statusCode)
19         when (statusCode) {
20             in 200..299 -> {
!!                ^ error
21                 val networkEither: NetworkEither<Nothing, *> =
22                     buildNetworkSuccess(data = response, code = code, headers = headers.toMap())
23                 proceedWith(HttpResponseContainer(subject.expectedType, networkEither))

```

* subprojects/network/network-core/common/main/kotlin/com/javiersc/either/network/ktor/_internal/interceptSuccessesAndReplaceWithNetworkEitherSuccess.kt:20:21
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
17         val response = subject.response
18         val code = NetworkEitherHttpStatusCode(statusCode)
19         when (statusCode) {
20             in 200..299 -> {
!!                     ^ error
21                 val networkEither: NetworkEither<Nothing, *> =
22                     buildNetworkSuccess(data = response, code = code, headers = headers.toMap())
23                 proceedWith(HttpResponseContainer(subject.expectedType, networkEither))

```

### style, UnusedPrivateMember (1)

Private member is unused and should be removed.

[Documentation](https://detekt.dev/docs/rules/style#unusedprivatemember)

* subprojects/network/network-core/common/test/kotlin/com/javiersc/either/network/ktor/KtorPluginTest.kt:5:13
```
Private property `TODO` is unused.
```
```kotlin
2 
3 import io.ktor.client.request.get
4 
5 private val TODO: String
!             ^ error
6     get() =
7         """
8             |{

```

generated with [detekt version 1.21.0](https://detekt.dev/) on 2022-07-22 21:58:41 UTC

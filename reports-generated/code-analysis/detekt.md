# detekt

## Metrics

* 202 number of properties

* 158 number of functions

* 28 number of classes

* 16 number of packages

* 54 number of kt files

## Complexity Report

* 2,813 lines of code (loc)

* 2,330 source lines of code (sloc)

* 1,599 logical lines of code (lloc)

* 36 comment lines of code (cloc)

* 297 cyclomatic complexity (mcc)

* 118 cognitive complexity

* 24 number of total code smells

* 1% comment source ratio

* 185 mcc per 1,000 lloc

* 15 code smells per 1,000 lloc

## Findings (24)

### complexity, TooManyFunctions (1)

Too many functions inside a/an file/class/object/interface always indicate a violation of the single responsibility principle. Maybe the file/class/object/interface wants to manage too many things at once. Extract functionality which clearly belongs together.

[Documentation](https://detekt.dev/docs/rules/complexity#toomanyfunctions)

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/NetworkEither.kt:8:21
```
Class 'NetworkEither' with '21' functions detected. Defined threshold inside classes is set to '11'
```
```kotlin
5  import arrow.core.right
6  import kotlin.contracts.contract
7  
8  public sealed class NetworkEither<out F, out S> {
!                      ^ error
9  
10     public sealed class Failure<out F> : NetworkEither<F, Nothing>() {
11 

```

### exceptions, SwallowedException (1)

The caught exception is swallowed. The original exception could be lost.

[Documentation](https://detekt.dev/docs/rules/exceptions#swallowedexception)

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:17:22
```
The caught exception is swallowed. The original exception could be lost.
```
```kotlin
14         } else {
15             try {
16                 proceed()
17             } catch (throwable: Throwable) {
!!                      ^ error
18                 val outgoingContent = RemoteErrorOutgoing
19                 proceedWith(fakeFailureHttpClientCall(scope, outgoingContent))
20             }

```

### exceptions, TooGenericExceptionCaught (2)

The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptioncaught)

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptBeforeTransformAndReplaceContainerWithNetowrkEitherType.kt:35:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
32 
33         try {
34             proceedWith(modifiedContainer)
35         } catch (throwable: Throwable) {
!!                  ^ error
36             val typeInfo = typeInfo<NetworkEither.Failure<Nothing>>()
37             val networkEither: NetworkEither<Any, Nothing> = unknownFailure(throwable)
38             proceedWith(HttpResponseContainer(typeInfo, networkEither))

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:17:22
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
14         } else {
15             try {
16                 proceed()
17             } catch (throwable: Throwable) {
!!                      ^ error
18                 val outgoingContent = RemoteErrorOutgoing
19                 proceedWith(fakeFailureHttpClientCall(scope, outgoingContent))
20             }

```

### naming, MatchingDeclarationName (1)

If a source file contains only a single non-private top-level class or object, the file name should reflect the case-sensitive name plus the .kt extension.

[Documentation](https://detekt.dev/docs/rules/naming#matchingdeclarationname)

* subprojects/resource-either/common/main/kotlin/com/javiersc/resource/either/Resource.kt:10:21
```
The file name 'Resource' does not match the name of the single top-level declaration 'ResourceEither'.
```
```kotlin
7  import arrow.core.right
8  import kotlin.contracts.contract
9  
10 public sealed class ResourceEither<out F, out S> {
!!                     ^ error
11 
12     public abstract val isLoading: Boolean
13 

```

### naming, PackageNaming (17)

Package names should match the naming convention set in the configuration.

[Documentation](https://detekt.dev/docs/rules/naming#packagenaming)

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/HttpStatusCodeExtensions.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import io.ktor.http.Headers
4 import io.ktor.http.HttpStatusCode

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/NetworkEitherFailureOutgoings.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import io.ktor.http.ContentType
4 import io.ktor.http.Headers

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/PipelineContextExtensions.kt:3:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 @file:Suppress("MagicNumber")
2 
3 package com.javiersc.network.either.ktor._internal
! ^ error
4 
5 import com.javiersc.kotlin.stdlib.secondOrNull
6 import com.javiersc.network.either.NetworkEither

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/fakeFailureHttpClientCall.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import io.ktor.client.HttpClient
4 import io.ktor.client.call.HttpClientCall

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptBeforeTransformAndReplaceContainerWithNetowrkEitherType.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import com.javiersc.network.either.NetworkEither
4 import com.javiersc.network.either.NetworkEither.Companion.localFailure

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptExceptionsAndReplaceWithNetworkEitherFailures.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import io.ktor.client.HttpClient
4 import io.ktor.client.request.HttpSendPipeline

```

* subprojects/network-either/common/main/kotlin/com/javiersc/network/either/ktor/_internal/interceptResponsesAndReplaceWithNetworkEither.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either.ktor._internal
! ^ error
2 
3 import com.javiersc.network.either.NetworkEither
4 import com.javiersc.network.either.NetworkEither.Companion.httpFailure

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/fakes/CodeArb.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config.fakes
! ^ error
2 
3 import io.kotest.property.Arb
4 import io.kotest.property.arbitrary.filter

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/fakes/Headers.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config.fakes
! ^ error
2 
3 import com.javiersc.network.either.Headers
4 import com.javiersc.network.either._config.readResource

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/fakes/Json.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config.fakes
! ^ error
2 
3 import kotlinx.serialization.json.Json
4 

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/fileSystem.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config
! ^ error
2 
3 import okio.FileSystem
4 

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/models/DogDTO.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config.models
! ^ error
2 
3 import kotlinx.serialization.Serializable
4 

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/models/ErrorDTO.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config.models
! ^ error
2 
3 import kotlinx.serialization.Serializable
4 

```

* subprojects/network-either/common/test/kotlin/com/javiersc/network/either/_config/readResource.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config
! ^ error
2 
3 import okio.Path
4 import okio.Path.Companion.toPath

```

* subprojects/network-either/jvm/test/kotlin/com/javiersc/network/either/_config/DogService.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config
! ^ error
2 
3 import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
4 import com.javiersc.network.either.NetworkEither

```

* subprojects/network-either/jvm/test/kotlin/com/javiersc/network/either/_config/MockResponseUtil.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config
! ^ error
2 
3 import com.javiersc.network.either.Headers
4 import org.mockserver.model.HttpResponse

```

* subprojects/network-either/jvm/test/kotlin/com/javiersc/network/either/_config/fileSystem.jvm.kt:1:1
```
Package name should match the pattern: [a-z]+(\.[a-z][A-Za-z0-9]*)*
```
```kotlin
1 package com.javiersc.network.either._config
! ^ error
2 
3 import okio.FileSystem
4 

```

### performance, SpreadOperator (2)

In most cases using a spread operator causes a full copy of the array to be created before calling a method. This may result in a performance penalty.

[Documentation](https://detekt.dev/docs/rules/performance#spreadoperator)

* subprojects/network-either/jvm/main/kotlin/com/javiersc/network/either/internal/utils/RetrofitUtils.kt:16:22
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

* subprojects/network-either/jvm/main/kotlin/com/javiersc/network/either/internal/utils/RetrofitUtils.kt:19:22
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

generated with [detekt version 1.21.0](https://detekt.dev/) on 2022-10-12 23:04:10 UTC
@file:Suppress("MagicNumber")

package com.javiersc.network.either.ktor._internal

import com.javiersc.kotlin.stdlib.secondOrNull
import com.javiersc.network.either.NetworkEither
import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.reflect.TypeInfo
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.typeOf

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.networkEitherTypeInfo: TypeInfo
    get() {
        val typeInfo =
            when (context.response.status.value) {
                in SUCCESS_RANGE -> successTypeInfo
                in ERROR_RANGE -> failureTypeInfo
                else -> null
            }
        return checkNotNull(typeInfo) { "TypeInfo must not be null, this should not be happening" }
    }

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.failureTypeInfo: TypeInfo?
    get() =
        if (isNetworkEither) {
            val failureKotlinType: KType =
                checkNotNull(subject.expectedType.kotlinType?.arguments?.firstOrNull()?.type)
            val failureType: KClass<*> = failureKotlinType.classifier as KClass<*>

            TypeInfo(type = failureType, kotlinType = failureKotlinType)
        } else null

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.successTypeInfo: TypeInfo?
    get() =
        if (isNetworkEither) {
            val successKotlinType: KType =
                checkNotNull(subject.expectedType.kotlinType?.arguments?.secondOrNull()?.type)
            val successType: KClass<*> = successKotlinType.classifier as KClass<*>

            TypeInfo(type = successType, kotlinType = successKotlinType)
        } else null

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEither: Boolean
    get() =
        (kClassifier == networkEitherKClassifier) ||
            isNetworkEitherFailure ||
            isNetworkEitherSuccess ||
            isNetworkEitherFailureLocal ||
            isNetworkEitherFailureRemote

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherFailureLocal:
    Boolean
    get() = kClassifier == networkEitherFailureLocalKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherFailureRemote:
    Boolean
    get() = kClassifier == networkEitherFailureRemoteKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherFailure: Boolean
    get() = kClassifier == networkEitherFailureKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherSuccess: Boolean
    get() = kClassifier == networkEitherSuccessKClassifier

internal val networkEitherFailureLocalKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither.Failure.Local>().classifier) {
            "`NetworkEither.Failure.Local should be denotable in Kotlin"
        }

internal val networkEitherFailureRemoteKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither.Failure.Remote>().classifier) {
            "`NetworkEither.Failure.Local should be denotable in Kotlin"
        }

internal val networkEitherFailureKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither.Failure<*>>().classifier) {
            "`NetworkEither.Failure should be denotable in Kotlin"
        }

internal val networkEitherSuccessKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither.Success<*>>().classifier) {
            "`NetworkEither.Failure should be denotable in Kotlin"
        }

internal val networkEitherKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither<*, *>>().classifier) {
            "`NetworkEither should be denotable in Kotlin"
        }

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.kClassifier: KClassifier?
    get() = subject.expectedType.kotlinType?.classifier

internal val PipelineContext<
    HttpResponseContainer,
    HttpClientCall,
>.requestContentIsNetworkFailureLocal: Boolean
    get() = context.request.content is LocalErrorOutgoing

internal val PipelineContext<
    HttpResponseContainer,
    HttpClientCall,
>.requestContentIsNetworkFailureRemote: Boolean
    get() = context.request.content is RemoteErrorOutgoing

internal val SUCCESS_RANGE: IntRange = 200..299

internal val ERROR_RANGE: IntRange = 400..599

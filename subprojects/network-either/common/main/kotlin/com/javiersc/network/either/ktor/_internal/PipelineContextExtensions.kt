@file:Suppress("MagicNumber")

package com.javiersc.network.either.ktor._internal

import com.javiersc.kotlin.stdlib.secondOrNull
import com.javiersc.network.either.NetworkEither
import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.reflect.Type
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
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
            val failureReifiedType: Type = failureKotlinType.platformType

            TypeInfo(
                type = failureType,
                reifiedType = failureReifiedType,
                kotlinType = failureKotlinType,
            )
        } else null

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.successTypeInfo: TypeInfo?
    get() =
        if (isNetworkEither) {
            println()
            val successKotlinType: KType =
                checkNotNull(subject.expectedType.kotlinType?.arguments?.secondOrNull()?.type)
            val successType: KClass<*> = successKotlinType.classifier as KClass<*>
            val successReifiedType: Type = successKotlinType.platformType

            TypeInfo(
                type = successType,
                reifiedType = successReifiedType,
                kotlinType = successKotlinType,
            )
        } else null

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEither: Boolean
    get() =
        (kClassifier == networkEitherKClassifier) ||
            isNetworkEitherFailure ||
            isNetworkEitherSuccess

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherFailure: Boolean
    get() = kClassifier == networkEitherFailureKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEitherSuccess: Boolean
    get() = kClassifier == networkEitherSuccessKClassifier

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

internal val SUCCESS_RANGE: IntRange = 200..299

internal val ERROR_RANGE: IntRange = 400..599

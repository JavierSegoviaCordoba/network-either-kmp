package com.javiersc.either.network.ktor._internal

import com.javiersc.either.Either
import com.javiersc.either.network.NetworkEither
import io.ktor.client.call.HttpClientCall
import io.ktor.client.statement.HttpResponse
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
                in successRange -> successTypeInfo
                in errorRange -> failureTypeInfo
                else -> null
            }
        return checkNotNull(typeInfo) { "TypeInfo must not be null, this should not be happening" }
    }

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.failureTypeInfo: TypeInfo?
    get() =
        if (isNetworkEither) {
            val failureKotlinType: KType =
                checkNotNull(
                    subject.expectedType.kotlinType
                        ?.arguments
                        ?.get(0)
                        ?.type
                        ?.arguments
                        ?.get(0)
                        ?.type
                )
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
            val successKotlinType: KType =
                checkNotNull(
                    subject.expectedType.kotlinType
                        ?.arguments
                        ?.get(1)
                        ?.type
                        ?.arguments
                        ?.get(0)
                        ?.type
                )
            val successType: KClass<*> = successKotlinType.classifier as KClass<*>
            val successReifiedType: Type = successKotlinType.platformType

            TypeInfo(
                type = successType,
                reifiedType = successReifiedType,
                kotlinType = successKotlinType,
            )
        } else null

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isEither: Boolean
    get() = isEitherLeft || isEitherRight

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isEitherLeft: Boolean
    get() = kClassifier == eitherLeftKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isEitherRight: Boolean
    get() = kClassifier == eitherRightKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.isNetworkEither: Boolean
    get() = kClassifier == networkEitherKClassifier

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.kClassifier: KClassifier?
    get() = subject.expectedType.kotlinType?.classifier

internal val eitherLeftKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<Either.Left<*>>().classifier) {
            "`NetworkEither should be denotable in Kotlin"
        }

internal val eitherRightKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<Either.Right<*>>().classifier) {
            "`NetworkEither should be denotable in Kotlin"
        }

internal val networkEitherKClassifier: KClassifier
    get() =
        checkNotNull(typeOf<NetworkEither<*, *>>().classifier) {
            "`NetworkEither should be denotable in Kotlin"
        }

internal val PipelineContext<HttpResponseContainer, HttpClientCall>.httpResponse: HttpResponse
    get() = context.response

internal val successRange: IntRange = 200..299
internal val errorRange: IntRange = 400..599

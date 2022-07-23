package com.javiersc.network.either.ktor._internal

import com.javiersc.network.either.NetworkEither
import com.javiersc.network.either.NetworkEither.Companion.unknownFailure
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.pipeline.PipelinePhase
import io.ktor.util.reflect.typeInfo
import kotlin.reflect.typeOf

internal fun interceptBeforeTransformAndReplaceContainerWithNetworkEitherType(scope: HttpClient) {
    val beforeTransformPipelinePhase = PipelinePhase("NetworkEitherBeforeTransform")
    scope.responsePipeline.insertPhaseBefore(
        HttpResponsePipeline.Transform,
        beforeTransformPipelinePhase
    )
    scope.responsePipeline.intercept(beforeTransformPipelinePhase) { container ->
        if (!isNetworkEither) return@intercept

        val response =
            if (networkEitherTypeInfo.kotlinType != typeOf<Unit>()) container.response else Unit

        val modifiedContainer =
            HttpResponseContainer(expectedType = networkEitherTypeInfo, response = response)

        try {
            proceedWith(modifiedContainer)
        } catch (throwable: Throwable) {
            val typeInfo = typeInfo<NetworkEither.Failure<Nothing>>()
            val networkEither: NetworkEither<Any, Nothing> = unknownFailure(throwable)
            proceedWith(HttpResponseContainer(typeInfo, networkEither))
        }
    }
}

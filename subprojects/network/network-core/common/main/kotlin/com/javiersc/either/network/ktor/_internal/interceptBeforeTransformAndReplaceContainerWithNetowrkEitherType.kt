package com.javiersc.either.network.ktor._internal

import com.javiersc.either.Either
import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureUnknown
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
            HttpResponseContainer(
                expectedType = networkEitherTypeInfo,
                response = response,
            )

        try {
            proceedWith(modifiedContainer)
        } catch (throwable: Throwable) {
            val typeInfo = typeInfo<Either.Left<Nothing>>()
            val networkEither: NetworkEither<Any, Nothing> = buildNetworkFailureUnknown(throwable)
            proceedWith(HttpResponseContainer(typeInfo, networkEither))
        }
    }
}

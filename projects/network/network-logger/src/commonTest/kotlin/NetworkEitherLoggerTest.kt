import com.javiersc.either.network.NetworkEither
import com.javiersc.either.network.buildNetworkFailureHttp
import com.javiersc.either.network.buildNetworkFailureLocal
import com.javiersc.either.network.buildNetworkFailureRemote
import com.javiersc.either.network.buildNetworkFailureUnknown
import com.javiersc.either.network.buildNetworkSuccess
import com.javiersc.either.network.logger.alsoPrettyPrint
import io.kotest.matchers.shouldBe
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlinx.serialization.Serializable
import kotlin.test.Test

internal class NetworkEitherLoggerTest {

    private val headers = headersOf("ContentType" to listOf("application/json")).toMap()

    @Test
    fun `Pretty print Success`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = buildNetworkSuccess(DogDTO("Auri"), 200, headers)
        val tag = "Success test"

        networkResponse.alsoPrettyPrint(tag, ErrorDTO.serializer(), DogDTO.serializer()) shouldBe networkResponse
        networkResponse.alsoPrettyPrint(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Http failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureHttp(ErrorDTO(":("), 404, headers)
        val tag = "Error test"

        networkResponse.alsoPrettyPrint(tag, ErrorDTO.serializer(), DogDTO.serializer()) shouldBe networkResponse
        networkResponse.alsoPrettyPrint(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Local failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureLocal()
        val tag = "Internet not available test"

        networkResponse.alsoPrettyPrint(tag, ErrorDTO.serializer(), DogDTO.serializer()) shouldBe networkResponse
        networkResponse.alsoPrettyPrint(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Remote failure`() {
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureRemote()
        val tag = "Remote not available test"

        networkResponse.alsoPrettyPrint(tag, ErrorDTO.serializer(), DogDTO.serializer()) shouldBe networkResponse
        networkResponse.alsoPrettyPrint(tag) shouldBe networkResponse
    }

    @Test
    fun `Pretty print Unknown failure`() {
        val exception = IllegalStateException("Unknown error")
        val networkResponse: NetworkEither<ErrorDTO, DogDTO> = buildNetworkFailureUnknown(exception)
        val tag = "Unknown error test"

        networkResponse.alsoPrettyPrint(tag, ErrorDTO.serializer(), DogDTO.serializer()) shouldBe networkResponse
        networkResponse.alsoPrettyPrint(tag) shouldBe networkResponse
    }
}

@Serializable
private data class DogDTO(val name: String)

@Serializable
private data class ErrorDTO(val error: String)

package examples.parameterNameClash.client

import com.fasterxml.jackson.databind.ObjectMapper
import examples.parameterNameClash.models.SomeObject
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import okhttp3.OkHttpClient
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map
import kotlin.jvm.Throws

/**
 * The circuit breaker registry should have the proper configuration to correctly action on circuit
 * breaker transitions based on the client exceptions [ApiClientException], [ApiServerException] and
 * [IOException].
 *
 * @see ApiClientException
 * @see ApiServerException
 */
@Suppress("unused")
class ExampleService(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,
    objectMapper: ObjectMapper,
    baseUrl: String,
    client: OkHttpClient
) {
    var circuitBreakerName: String = "exampleClient"

    private val apiClient: ExampleClient = ExampleClient(objectMapper, baseUrl, client)

    @Throws(ApiException::class)
    fun getExampleB(
        pathB: String,
        queryB: String,
        additionalHeaders: Map<String, String> = emptyMap()
    ): ApiResponse<Unit> =
        withCircuitBreaker(circuitBreakerRegistry, circuitBreakerName) {
            apiClient.getExampleB(pathB, queryB, additionalHeaders)
        }

    @Throws(ApiException::class)
    fun postExample(
        bodySomeObject: SomeObject,
        querySomeObject: String,
        additionalHeaders: Map<String, String> = emptyMap()
    ): ApiResponse<Unit> =
        withCircuitBreaker(circuitBreakerRegistry, circuitBreakerName) {
            apiClient.postExample(bodySomeObject, querySomeObject, additionalHeaders)
        }
}

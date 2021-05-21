package dali.hamza.domain.models

open class IResponse

sealed class Response<T>(val data: T?, val error: PokeError?) : IResponse() {
    class SuccessResponse<T>(data: T) : Response<T>(data, null)
    class ErrorResponse<T>(error: PokeError) : Response<T>(null, error = error)
}

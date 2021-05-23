package dali.hamza.domain.models

open class IResponse

sealed class MyResponse<T>(val data: T?, val error: PokeError?) : IResponse() {
    class SuccessResponse<T>(data: T) : MyResponse<T>(data, null)
    class ErrorResponse<T>(error: PokeError) : MyResponse<T>(null, error = error)
}

inline fun <T : Any> MyResponse<T>.onSuccess(action: (T) -> Unit): MyResponse<T> {
    if (this is MyResponse.SuccessResponse) action(data!!)
    return this
}

inline fun <T : Any> MyResponse<T>.onFailure(action: (PokeError) -> Unit) {
    if (this is MyResponse.ErrorResponse) action(error!!)
}

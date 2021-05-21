package dali.hamza.domain.models

open class PokeError(val error: Any?)
class MessageError(error: Any?) : PokeError(error = error)
class DeleteError(error: Any?) : PokeError(error = error)
class ListError(error: Any?) : PokeError(error = error)
class EmptyData(error: Any?) : PokeError(error = error)
class CreateDataError(error: Any?) : PokeError(error = error)
class AuthError(error: Any?) : PokeError(error = error)
class NetworkError(error: Any?) : PokeError(error = error)
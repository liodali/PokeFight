package dali.hamza.domain.models

data class UserPokemon(
    val pokemon: Pokemon,
    val name:String,
)
data class Community(
    val name:String,
    val listUserPokemon: List<UserPokemon>,
)
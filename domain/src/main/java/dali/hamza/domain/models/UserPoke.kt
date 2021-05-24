package dali.hamza.domain.models

data class UserPokemon(
    val name:String,
    val pokemon: Pokemon,
)
data class Community(
    val name:String,
    val listUserPokemon: List<UserPokemon>,
)
package dali.hamza.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPokemon(
    val pokemon: Pokemon,
    val name: String,
    val typeCommunity: String,
) : Parcelable

@Parcelize
data class Community(
    val name: String,
    val listUserPokemon: List<UserPokemon>,
) : Parcelable
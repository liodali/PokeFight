package dali.hamza.core.utilities

import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.PokemonWithGeoPoint

fun PokemonApiData.toPokemonWithGeoPoint(
    userGeoPoint: PokeGeoPoint
): PokemonWithGeoPoint {
    val len = this.url.split("/").size
    return PokemonWithGeoPoint(
        id = this.url.split("/")[len - 2].toInt(),
        name = name,
        url = url,
        pokeGeoPoint = getLocationInLatLngRad(
            radiusInMeters = 1000.0,
            currentLocation = userGeoPoint
        )
    )
}
package dali.hamza.core.utilities

import dali.hamza.core.datasource.db.entities.PokemonWithGeoPointEntity
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint

fun PokemonApiData.toPokemonWithGeoPoint(
    userGeoPoint: PokeGeoPoint
): PokemonWithGeoPoint {
    val len = this.url.split("/").size
    return PokemonWithGeoPoint(
        pokemon = Pokemon(
            id = this.url.split("/")[len - 2].toInt(),
            name = name,
            url = url,
            captured_at = ""
        ),
        pokeGeoPoint = getLocationInLatLngRad(
            radiusInMeters = 1000.0,
            currentLocation = userGeoPoint
        )
    )
}

fun PokemonWithGeoPointEntity.toPokemonWithGeoPoint(): PokemonWithGeoPoint {
    return PokemonWithGeoPoint(
        pokemon = Pokemon(
            id = this.pokemonEntity.id,
            name = this.pokemonEntity.name,
            url = "https://pokeapi.co/api/v2/pokemon/${this.pokemonEntity.id}/",
            captured_at = DateManager.dateFormat_full.format(this.pokemonEntity.capturedAt)
        ),
        pokeGeoPoint = PokeGeoPoint(
            lat = this.geoPointEntity.lat,
            lon = this.geoPointEntity.lon
        )
    )
}
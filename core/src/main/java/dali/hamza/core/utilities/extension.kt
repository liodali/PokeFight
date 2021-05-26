package dali.hamza.core.utilities

import dali.hamza.core.datasource.db.entities.PokemonEntity
import dali.hamza.core.datasource.db.entities.PokemonWithGeoPointEntity
import dali.hamza.core.datasource.network.models.MyPokemonTeamApi
import dali.hamza.core.datasource.network.models.PokeApiData
import dali.hamza.domain.models.DetailPokemon
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import java.util.*

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

fun PokemonWithGeoPoint.toMyPokemonTeamApi(): MyPokemonTeamApi {
    return MyPokemonTeamApi(
        id = this.pokemon.id,
        name = this.pokemon.name,
        captured_at = "",
        captured_lat_at = this.pokeGeoPoint.lat,
        captured_long_at = this.pokeGeoPoint.lon,
    )
}

fun Pokemon.toPokemonDb(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        capturedAt = DateManager.dateFormat_full.parse(this.captured_at)!!
    )
}

fun MyPokemonTeamApi.toPokemonWithGeoPoint(): PokemonWithGeoPoint {
    return PokemonWithGeoPoint(
        pokemon = Pokemon(
            id = this.id,
            name = this.name,
            url = "https://pokeapi.co/api/v2/pokemon/${this.id}/",
            captured_at = DateManager.dateFormat_full.format(
                Date(
                    DateManager.convertStringFromFormatApiToApp(
                        this.captured_at
                    )
                )
            )
        ),
        pokeGeoPoint = PokeGeoPoint(
            lat = this.captured_lat_at,
            lon = this.captured_long_at
        )
    )
}

fun PokeApiData.toDetailPokemon(): DetailPokemon {
    return DetailPokemon(
        hp = stats.first {
            it.stat.name == "hp"
        }.base_stat,
        moves = moves.shuffled().subList(0, 4).map {
            it.move.name
        },
        types = types.map { type ->
            type.type.name
        }
    )
}
package dali.hamza.pokemongofight.common

import dali.hamza.domain.models.PokeGeoPoint
import org.osmdroid.util.GeoPoint

fun GeoPoint.toPokemonGeoPoint():PokeGeoPoint{
    return PokeGeoPoint(
        lon = longitude,
        lat =  latitude
    )
}
fun PokeGeoPoint.toGeoPoint():GeoPoint{
    return GeoPoint(
        lat,lon
    )
}
package dali.hamza.pokemongofight.common

import android.view.View
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

fun View.visible() {
    visibility = View.VISIBLE
}
fun View.enabled(){
    isEnabled=true
}

fun View.disabled(){
    isEnabled=false
}

fun View.gone() {
    visibility = View.GONE
}
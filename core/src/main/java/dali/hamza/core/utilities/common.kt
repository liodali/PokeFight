package dali.hamza.core.utilities

import dali.hamza.domain.models.PokeGeoPoint
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun getLocationInLatLngRad(radiusInMeters: Double, currentLocation: PokeGeoPoint): PokeGeoPoint {
    val x0: Double = currentLocation.lon
    val y0: Double = currentLocation.lat

    // Convert radius from meters to degrees.
    val radiusInDegrees = radiusInMeters / 111320f

    // Get a random distance and a random angle.
    val u: Double = Random.nextDouble()
    val v: Double = Random.nextDouble()
    val w = radiusInDegrees * sqrt(u)
    val t = 2 * Math.PI * v
    // Get the x and y delta values.
    val x = w * cos(t)
    val y = w * sin(t)

    // Compensate the x value.
    val newX = x / cos(Math.toRadians(y0))
    val foundLatitude: Double = y0 + y
    val foundLongitude: Double = x0 + newX

    return PokeGeoPoint(foundLongitude, foundLatitude)
}
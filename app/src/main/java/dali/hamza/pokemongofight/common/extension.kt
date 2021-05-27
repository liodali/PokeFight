package dali.hamza.pokemongofight.common

import android.view.View
import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.MyResponse
import dali.hamza.domain.models.PokeError
import dali.hamza.domain.models.PokeGeoPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import org.osmdroid.util.GeoPoint

fun GeoPoint.toPokemonGeoPoint(): PokeGeoPoint {
    return PokeGeoPoint(
        lon = longitude,
        lat = latitude
    )
}

fun PokeGeoPoint.toGeoPoint(): GeoPoint {
    return GeoPoint(
        lat, lon
    )
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disabled() {
    isEnabled = false
}

fun View.gone() {
    visibility = View.GONE
}

@OptIn(InternalCoroutinesApi::class)
 suspend inline fun Flow<IResponse?>.onData(
    crossinline error:  (value: PokeError) -> Unit,
    crossinline success: suspend (value: MyResponse.SuccessResponse<*>) -> Unit,
): Unit =
    collect(object : FlowCollector<IResponse?> {
        override suspend fun emit(value: IResponse?) {
            if (value!= null && value is MyResponse.ErrorResponse<*>) {
                error(value.error!!)
            }
            if ( value != null && value is MyResponse.SuccessResponse<*> ) {
                success(value)
            }
        }
    })



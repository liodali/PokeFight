package dali.hamza.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokeGeoPoint(
    val lon:Double,
    val lat:Double
):Parcelable

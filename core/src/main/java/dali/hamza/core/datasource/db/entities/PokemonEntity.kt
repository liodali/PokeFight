package dali.hamza.core.datasource.db.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "Pokemon"
)
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val capturedAt: Date,
)

@Entity(
    tableName = "PokemonGeoPoint",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE

        )
    ]
)
data class GeoPointEntity(
    @PrimaryKey val pokemonId: Int,
    val lat: Double,
    val lon: Double,
)

data class PokemonWithGeoPointEntity(
    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        entityColumn = "id",
        parentColumn = "pokemonId"
    ) val geoPointEntity: GeoPointEntity
)

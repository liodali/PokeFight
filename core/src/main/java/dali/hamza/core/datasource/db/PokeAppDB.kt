package dali.hamza.core.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dali.hamza.core.datasource.db.daos.PokemonDao
import dali.hamza.core.datasource.db.entities.GeoPointEntity
import dali.hamza.core.datasource.db.entities.PokemonEntity
import dali.hamza.core.datasource.db.utilities.Converter

@Database(
    version = 1,
    entities = [
        PokemonEntity::class,
        GeoPointEntity::class,
    ],
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class PokeAppDB : RoomDatabase() {
    abstract fun PokemonDao(): PokemonDao
}
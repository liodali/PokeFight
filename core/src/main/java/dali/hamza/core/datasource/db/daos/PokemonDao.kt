package dali.hamza.core.datasource.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dali.hamza.core.datasource.db.entities.PokemonEntity
import dali.hamza.core.datasource.db.entities.PokemonWithGeoPointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao : AppDao<PokemonEntity> {

    @Query("select * from pokemon where id=:idP")
    fun getPokemonById(idP: Int): Flow<List<PokemonEntity>>

    @Transaction
    @Query("select * from pokemon p , pokemongeopoint g where p.id=:idP and p.id==g.pokemonId")
    fun getPokemonWithGeoPointById(idP: Int): Flow<PokemonWithGeoPointEntity>

    @Transaction
    @Query("select * from pokemon p , pokemongeopoint g where  p.id==g.pokemonId")
    fun getPokemonWithGeoPoint(): Flow<List<PokemonWithGeoPointEntity>>


}
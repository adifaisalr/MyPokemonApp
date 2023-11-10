package com.adifaisalr.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adifaisalr.core.domain.model.Pokemon

/**
 * Interface for database access.
 */
@Dao
abstract class PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPokemon(pokemon: Pokemon): Long

    @Query("DELETE FROM `Pokemon` WHERE id = :id")
    abstract suspend fun deletePokemon(id: Int): Int

    @Query("SELECT * FROM `Pokemon`")
    abstract fun loadAllPokemons(): List<Pokemon>

    @Query("SELECT * FROM `Pokemon` WHERE id = :id")
    abstract fun loadPokemonById(id: Int): Pokemon?

    @Update
    abstract suspend fun updatePokemon(Pokemon: Pokemon): Int
}

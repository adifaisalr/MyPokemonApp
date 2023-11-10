package com.adifaisalr.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adifaisalr.core.domain.model.Pokemon

/**
 * Main database description.
 */
@Database(
    entities = [
        Pokemon::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PokemonDb : RoomDatabase() {
    abstract fun dao(): PokemonDao
}

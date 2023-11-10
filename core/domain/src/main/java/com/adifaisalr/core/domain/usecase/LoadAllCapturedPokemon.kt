package com.adifaisalr.core.domain.usecase

import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadCapturedPokemon @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend fun loadAll(): List<Pokemon> =
        withContext(Dispatchers.IO) {
            return@withContext pokemonRepository.loadAllPokemonLocally()
        }

    suspend fun loadById(id: Int): Pokemon? =
        withContext(Dispatchers.IO) {
            return@withContext pokemonRepository.loadPokemonLocallyById(id)
        }
}
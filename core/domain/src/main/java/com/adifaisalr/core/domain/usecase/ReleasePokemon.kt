package com.adifaisalr.core.domain.usecase

import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReleasePokemon @Inject constructor(private val repository: PokemonRepository) {

    suspend operator fun invoke(pokemon: Pokemon): Int = withContext(Dispatchers.IO) {
        return@withContext repository.deletePokemon(pokemon)
    }
}
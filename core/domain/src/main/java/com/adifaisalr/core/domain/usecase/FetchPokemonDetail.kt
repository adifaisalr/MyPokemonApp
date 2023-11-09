package com.adifaisalr.core.domain.usecase

import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchPokemonDetail @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): Results<Pokemon> =
        withContext(Dispatchers.IO) {
            return@withContext pokemonRepository.getPokemonDetail(id)
        }
}
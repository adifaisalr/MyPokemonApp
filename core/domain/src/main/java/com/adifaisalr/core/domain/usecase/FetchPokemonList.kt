package com.adifaisalr.core.domain.usecase

import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchPokemonList @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(offset: Int, limit: Int): Results<NamedApiResourceList> =
        withContext(Dispatchers.IO) {
            return@withContext pokemonRepository.getPokemonList(offset, limit)
        }
}
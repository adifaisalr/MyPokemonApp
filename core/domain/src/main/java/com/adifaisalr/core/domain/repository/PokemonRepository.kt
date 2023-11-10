package com.adifaisalr.core.domain.repository

import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): Results<NamedApiResourceList>
    suspend fun getPokemonDetail(id: Int): Results<Pokemon>
    suspend fun savePokemon(pokemon: Pokemon): Long
    suspend fun deletePokemon(pokemon: Pokemon): Int
    suspend fun loadAllPokemonLocally(): List<Pokemon>
    suspend fun loadPokemonLocallyById(id: Int): Pokemon?
}
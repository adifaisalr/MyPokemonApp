package com.adifaisalr.core.data.repository

import com.adifaisalr.core.data.api.PokemonApi
import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi
):PokemonRepository {
    override suspend fun getPokemonList(offset: Int, limit: Int): Results<NamedApiResourceList> {
        return pokemonApi.getPokemonList(offset,limit)
    }

    override suspend fun getPokemonDetail(id: Int): Results<Pokemon> {
        return pokemonApi.getPokemonDetail(id)
    }
}
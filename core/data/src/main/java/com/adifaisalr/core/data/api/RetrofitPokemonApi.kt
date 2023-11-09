package com.adifaisalr.core.data.api

import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import javax.inject.Inject

class RetrofitPokemonApi @Inject constructor(
    private val service: PokemonService,
) : PokemonApi {
    override suspend fun getPokemonList(offset: Int, limit: Int): Results<NamedApiResourceList> {
        return service.getPokemonList(offset, limit)
    }

    override suspend fun getPokemonDetail(id: Int): Results<Pokemon> {
        return service.getPokemonDetail(id)
    }
}
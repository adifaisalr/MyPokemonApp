package com.adifaisalr.core.data.api

import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results

interface PokemonApi {
    suspend fun getPokemonList(offset: Int, limit: Int): Results<NamedApiResourceList>
    suspend fun getPokemonDetail(id: Int): Results<Pokemon>
}
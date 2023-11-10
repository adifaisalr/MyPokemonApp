package com.adifaisalr.core.data.repository

import com.adifaisalr.core.data.api.PokemonApi
import com.adifaisalr.core.data.db.PokemonDao
import com.adifaisalr.core.domain.model.NamedApiResourceList
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao,
) : PokemonRepository {
    override suspend fun getPokemonList(offset: Int, limit: Int): Results<NamedApiResourceList> {
        return pokemonApi.getPokemonList(offset, limit)
    }

    override suspend fun getPokemonDetail(id: Int): Results<Pokemon> {
        return pokemonApi.getPokemonDetail(id)
    }

    override suspend fun savePokemon(pokemon: Pokemon): Long {
        return pokemonDao.insertPokemon(pokemon)
    }

    override suspend fun deletePokemon(pokemon: Pokemon): Int {
        return pokemonDao.deletePokemon(pokemon.id)
    }

    override suspend fun loadAllPokemonLocally(): List<Pokemon> {
        return pokemonDao.loadAllPokemons()
    }

    override suspend fun loadPokemonLocallyById(id: Int): Pokemon? {
        return pokemonDao.loadPokemonById(id)
    }
}
package com.adifaisalr.mypokemonapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.usecase.FetchPokemonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val fetchPokemonList: FetchPokemonList,
) : ViewModel() {

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        fetchPokemonList(0, 20)
    }
}
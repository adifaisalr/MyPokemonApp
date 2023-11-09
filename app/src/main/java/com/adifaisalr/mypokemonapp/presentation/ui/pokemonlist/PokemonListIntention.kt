package com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist

import com.adifaisalr.core.domain.model.NamedApiResource
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel

data class PokemonListViewState(
    val itemList: List<NamedApiResource> = listOf(),
    val isLastBatch: Boolean = false,
    val isLoading: Boolean = false,
) : BaseViewModel.ViewState

sealed class PokemonListActionResult : BaseViewModel.ActionResult {
    data class SetShowLoading(val isShown: Boolean) : PokemonListActionResult()
    data class SetItemList(
        val itemList: List<NamedApiResource>,
        val isLastBatch: Boolean?,
    ) : PokemonListActionResult()

    data class GoToDetail(
        val id: Int,
    ) : PokemonListActionResult()

    data object DoNothing : PokemonListActionResult()
    data object RefreshState : PokemonListActionResult()
}
package com.adifaisalr.mypokemonapp.presentation.ui.captured

import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel

data class CapturedPokemonListViewState(
    val itemList: List<Pokemon> = listOf(),
    val isLoading: Boolean = false,
) : BaseViewModel.ViewState

sealed class CapturedPokemonListActionResult : BaseViewModel.ActionResult {
    data class SetShowLoading(val isShown: Boolean) : CapturedPokemonListActionResult()
    data class SetItemList(
        val itemList: List<Pokemon>,
    ) : CapturedPokemonListActionResult()
}
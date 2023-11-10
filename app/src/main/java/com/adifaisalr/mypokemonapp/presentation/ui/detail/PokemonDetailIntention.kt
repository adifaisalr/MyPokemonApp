package com.adifaisalr.mypokemonapp.presentation.ui.detail

import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel

data class PokemonDetailViewState(
    val pokemon: Pokemon? = null,
    val capturedSectionViewState: CapturedSectionViewState = CapturedSectionViewState(),
    val isLoading: Boolean = false,
    val error: String? = null,
) : BaseViewModel.ViewState

data class CapturedSectionViewState(
    val isCaptured: Boolean = false,
    val isLoading: Boolean = false,
) : BaseViewModel.ViewState

sealed class PokemonDetailActionResult : BaseViewModel.ActionResult {
    data class SetShowLoading(val isShown: Boolean) : PokemonDetailActionResult()
    data class SetError(val errorMsg: String?) : PokemonDetailActionResult()
    data class SetPokemon(
        val pokemon: Pokemon?,
    ) : PokemonDetailActionResult()

    data class SetCapturedSectionViewState(
        val capturedSectionViewState: CapturedSectionViewState,
    ) : PokemonDetailActionResult()

    data object DoNothing : PokemonDetailActionResult()
    data object RefreshState : PokemonDetailActionResult()
}
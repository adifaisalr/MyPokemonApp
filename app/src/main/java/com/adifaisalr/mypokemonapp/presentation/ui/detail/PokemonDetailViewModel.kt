package com.adifaisalr.mypokemonapp.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.usecase.FetchPokemonDetail
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPokemonDetailUseCase: FetchPokemonDetail,
) : BaseViewModel<PokemonDetailViewState, PokemonDetailActionResult>(
    initialState = PokemonDetailViewState()
) {

    fun setPokemonId(id: Int) {
        savedStateHandle[SAVED_STATE_ID] = id
    }

    fun fetchPokemonDetail() = viewModelScope.launch {
        val pokemonId = savedStateHandle.get<Int>(SAVED_STATE_ID) ?: return@launch
        handleActionResult(PokemonDetailActionResult.SetShowLoading(true))
        when (val result = fetchPokemonDetailUseCase(pokemonId)) {
            is Results.Success -> {
                handleActionResult(PokemonDetailActionResult.SetPokemon(result.data))
            }

            else -> {
                handleActionResult(PokemonDetailActionResult.SetError("Error fetching data"))
            }
        }
    }

    override fun reducer(
        oldState: PokemonDetailViewState,
        actionResult: PokemonDetailActionResult
    ): PokemonDetailViewState {
        return PokemonDetailViewState(
            pokemon = oldState.pokemonDetailReducer(actionResult),
            capturedSectionViewState = oldState.capturedSectionReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
            error = errorReducer(actionResult),
        )
    }

    private fun PokemonDetailViewState.pokemonDetailReducer(actionResult: PokemonDetailActionResult): Pokemon? {
        return when (actionResult) {
            is PokemonDetailActionResult.SetPokemon -> actionResult.pokemon
            is PokemonDetailActionResult.SetError -> null
            else -> pokemon
        }
    }

    private fun PokemonDetailViewState.capturedSectionReducer(actionResult: PokemonDetailActionResult): CapturedSectionViewState {
        return when (actionResult) {
            is PokemonDetailActionResult.SetCapturedSectionViewState -> actionResult.capturedSectionViewState
            else -> capturedSectionViewState
        }
    }

    private fun isLoadingReducer(actionResult: PokemonDetailActionResult): Boolean {
        return when (actionResult) {
            is PokemonDetailActionResult.SetShowLoading -> actionResult.isShown
            PokemonDetailActionResult.RefreshState -> true
            else -> false
        }
    }

    private fun errorReducer(actionResult: PokemonDetailActionResult): String? {
        return when (actionResult) {
            is PokemonDetailActionResult.SetError -> actionResult.errorMsg
            else -> null
        }
    }

    companion object {
        const val SAVED_STATE_ID = "pokemon_id"
    }
}
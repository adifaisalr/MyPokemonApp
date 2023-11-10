package com.adifaisalr.mypokemonapp.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.usecase.CapturePokemon
import com.adifaisalr.core.domain.usecase.FetchPokemonDetail
import com.adifaisalr.core.domain.usecase.LoadCapturedPokemon
import com.adifaisalr.core.domain.usecase.ReleasePokemon
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPokemonDetailUseCase: FetchPokemonDetail,
    private val capturePokemonUseCase: CapturePokemon,
    private val releasePokemonUseCase: ReleasePokemon,
    private val loadCapturedPokemonUseCase: LoadCapturedPokemon,
) : BaseViewModel<PokemonDetailViewState, PokemonDetailActionResult>(
    initialState = PokemonDetailViewState()
) {

    private var pokemon: Pokemon? = null
    protected var isCaptured = false

    fun setPokemonId(id: Int) {
        savedStateHandle[SAVED_STATE_ID] = id
    }

    fun fetchPokemonDetail() = viewModelScope.launch {
        val pokemonId = savedStateHandle.get<Int>(SAVED_STATE_ID) ?: return@launch
        handleActionResult(PokemonDetailActionResult.SetShowLoading(true))
        when (val result = fetchPokemonDetailUseCase(pokemonId)) {
            is Results.Success -> {
                pokemon = result.data
                handleActionResult(PokemonDetailActionResult.SetPokemon(result.data))
                loadCaptureStatus()
            }

            else -> {
                handleActionResult(PokemonDetailActionResult.SetError("Error fetching data"))
            }
        }
    }

    private fun loadCaptureStatus() = viewModelScope.launch {
        val pokemonId = savedStateHandle.get<Int>(SAVED_STATE_ID) ?: return@launch
        val favoriteMedia = loadCapturedPokemonUseCase.loadById(pokemonId)
        isCaptured = favoriteMedia != null
        handleActionResult(
            PokemonDetailActionResult.SetCapturedSectionViewState(
                CapturedSectionViewState(isCaptured = isCaptured)
            )
        )
    }

    fun changeCaptured() {
        if (isCaptured) releasePokemon()
        else capturedPokemon()
    }

    private fun capturedPokemon() = viewModelScope.launch {
        pokemon?.let {
            val res = capturePokemonUseCase(it)
            handleActionResult(
                PokemonDetailActionResult.SetCapturedSectionViewState(
                    CapturedSectionViewState(isCaptured = true)
                )
            )
        }
    }

    private fun releasePokemon() = viewModelScope.launch {
        pokemon?.let {
            val res = releasePokemonUseCase(it)
            handleActionResult(
                PokemonDetailActionResult.SetCapturedSectionViewState(
                    CapturedSectionViewState(isCaptured = false)
                )
            )
        }
    }

    override fun reducer(
        oldState: PokemonDetailViewState,
        actionResult: PokemonDetailActionResult
    ): PokemonDetailViewState {
        return PokemonDetailViewState(
            pokemon = pokemonDetailReducer(actionResult),
            capturedSectionViewState = oldState.capturedSectionReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
            error = errorReducer(actionResult),
        )
    }

    private fun pokemonDetailReducer(actionResult: PokemonDetailActionResult): Pokemon? {
        pokemon = when (actionResult) {
            is PokemonDetailActionResult.SetError -> null
            else -> pokemon
        }
        return pokemon
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
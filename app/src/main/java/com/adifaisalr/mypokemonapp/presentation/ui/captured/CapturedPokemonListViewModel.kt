package com.adifaisalr.mypokemonapp.presentation.ui.captured

import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.usecase.LoadCapturedPokemon
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CapturedPokemonListViewModel @Inject constructor(
    private val loadCapturedPokemonUseCase: LoadCapturedPokemon
) : BaseViewModel<CapturedPokemonListViewState, CapturedPokemonListActionResult>(
    initialState = CapturedPokemonListViewState()
) {
    protected var capturedPokemonList: List<Pokemon> = emptyList()

    fun loadData() {
        loadCapturedPokemon()
    }

    private fun loadCapturedPokemon() = viewModelScope.launch {
        handleActionResult(CapturedPokemonListActionResult.SetShowLoading(true))
        val capturedPokemonList = loadCapturedPokemonUseCase.loadAll()
        handleActionResult(CapturedPokemonListActionResult.SetItemList(capturedPokemonList))
    }

    override fun reducer(
        oldState: CapturedPokemonListViewState,
        actionResult: CapturedPokemonListActionResult
    ): CapturedPokemonListViewState {
        return CapturedPokemonListViewState(
            itemList = capturedPokemonListReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
        )
    }

    private fun capturedPokemonListReducer(actionResult: CapturedPokemonListActionResult): List<Pokemon> {
        capturedPokemonList = when (actionResult) {
            is CapturedPokemonListActionResult.SetShowLoading -> emptyList()
            is CapturedPokemonListActionResult.SetItemList -> actionResult.itemList
        }
        return capturedPokemonList
    }

    private fun isLoadingReducer(actionResult: CapturedPokemonListActionResult): Boolean {
        return when (actionResult) {
            is CapturedPokemonListActionResult.SetShowLoading -> actionResult.isShown
            else -> false
        }
    }
}
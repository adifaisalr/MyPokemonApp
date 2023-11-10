package com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist

import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.model.NamedApiResource
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.usecase.FetchPokemonList
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val fetchPokemonList: FetchPokemonList,
) : BaseViewModel<PokemonListViewState, PokemonListActionResult>(
    initialState = PokemonListViewState()
) {
    protected var itemList: List<NamedApiResource> = emptyList()

    fun loadNextPage(isRefreshing: Boolean = false) = viewModelScope.launch {
        if (isRefreshing) {
            handleActionResult(PokemonListActionResult.RefreshState)
        }
        handleActionResult(PokemonListActionResult.SetShowLoading(true))
        when (
            val response = fetchPokemonList(offset = itemList.count(), limit = PER_PAGE)
        ) {
            is Results.Success -> {
                val newData = response.data?.results ?: listOf()
                handleActionResult(
                    PokemonListActionResult.SetItemList(
                        itemList = newData,
                        isLastBatch = newData.size < PER_PAGE,
                    )
                )
            }

            else -> {}
        }
    }

    override fun reducer(
        oldState: PokemonListViewState,
        actionResult: PokemonListActionResult
    ): PokemonListViewState {
        return PokemonListViewState(
            itemList = itemListReducer(actionResult),
            isLoading = isLoadingReducer(actionResult),
            isLastBatch = oldState.isLastBatchReducer(actionResult),
        )
    }

    private fun itemListReducer(actionResult: PokemonListActionResult): List<NamedApiResource> {
        itemList = when (actionResult) {
            PokemonListActionResult.RefreshState -> emptyList()
            is PokemonListActionResult.SetItemList -> itemList + actionResult.itemList

            else -> itemList
        }
        return itemList
    }

    private fun isLoadingReducer(actionResult: PokemonListActionResult): Boolean {
        return when (actionResult) {
            is PokemonListActionResult.SetShowLoading -> actionResult.isShown
            PokemonListActionResult.RefreshState -> true
            else -> false
        }
    }

    private fun PokemonListViewState.isLastBatchReducer(actionResult: PokemonListActionResult): Boolean =
        (actionResult as? PokemonListActionResult.SetItemList)?.isLastBatch
            ?: isLastBatch

    companion object {
        const val PER_PAGE = 20
    }
}
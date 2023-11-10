package com.adifaisalr.mypokemonapp.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adifaisalr.core.domain.model.Pokemon
import com.adifaisalr.core.domain.model.dataholder.Results
import com.adifaisalr.core.domain.usecase.CapturePokemon
import com.adifaisalr.core.domain.usecase.FetchPokemonDetail
import com.adifaisalr.core.domain.usecase.LoadCapturedPokemon
import com.adifaisalr.core.domain.usecase.ReleasePokemon
import com.adifaisalr.core.domain.usecase.RenamePokemon
import com.adifaisalr.mypokemonapp.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPokemonDetailUseCase: FetchPokemonDetail,
    private val capturePokemonUseCase: CapturePokemon,
    private val releasePokemonUseCase: ReleasePokemon,
    private val loadCapturedPokemonUseCase: LoadCapturedPokemon,
    private val renamePokemonUseCase: RenamePokemon,
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
        val capturedPokemon = loadCapturedPokemonUseCase.loadById(pokemonId)
        isCaptured = capturedPokemon != null
        capturedPokemon?.let {
            pokemon?.name = it.name
        }
        handleActionResult(
            PokemonDetailActionResult.SetCapturedSectionViewState(
                CapturedSectionViewState(isCaptured = isCaptured)
            )
        )
    }

    fun renamePokemon() = viewModelScope.launch {
        pokemon?.let { poke ->
            var newName = poke.name
            val fibo = fibonacci(poke.renameCount)
            val stripIdx = poke.name.lastIndexOf('-')
            if (stripIdx > -1) {
                newName = poke.name.removeRange(stripIdx, poke.name.length)
            }
            newName += "-$fibo"

            val pokeCopy = poke.copy(renameCount = poke.renameCount + 1, name = newName)
            renamePokemonUseCase(pokeCopy)
            handleActionResult(PokemonDetailActionResult.SetPokemon(pokeCopy))
            handleActionResult(PokemonDetailActionResult.ShowToast("pokemon was renamed to ${newName}!"))
        }
    }

    fun changeCaptured() {
        if (isCaptured) releasePokemon()
        else capturedPokemon()
    }

    private fun capturedPokemon() = viewModelScope.launch {
        handleActionResult(PokemonDetailActionResult.DoNothing)
        pokemon?.let { poke ->
            val success = Random.nextBoolean()
            if (!success) {
                handleActionResult(PokemonDetailActionResult.ShowToast("Failed to catch ${poke.name}!"))
            } else {
                val res = capturePokemonUseCase(poke)
                handleActionResult(
                    PokemonDetailActionResult.SetCapturedSectionViewState(
                        CapturedSectionViewState(isCaptured = true)
                    )
                )
                handleActionResult(PokemonDetailActionResult.ShowToast("${poke.name} was captured!"))
            }
        }
    }

    private fun releasePokemon() = viewModelScope.launch {
        handleActionResult(PokemonDetailActionResult.DoNothing)
        pokemon?.let { poke ->
            val success = isPrime(Random.nextInt(until = 100))
            if (!success) {
                handleActionResult(PokemonDetailActionResult.ShowToast("Failed to release ${poke.name}!"))
            } else {
                val res = releasePokemonUseCase(poke)
                handleActionResult(
                    PokemonDetailActionResult.SetCapturedSectionViewState(
                        CapturedSectionViewState(isCaptured = false)
                    )
                )
                handleActionResult(PokemonDetailActionResult.ShowToast("${poke.name} was released!"))
            }
        }
    }

    private fun isPrime(number: Int): Boolean {
        if (number <= 1) {
            return false
        }

        for (i in 2 until number) {
            if (number % i == 0) {
                return false
            }
        }

        return true
    }

    private tailrec fun fibonacci(n: Int, a: Long = 0, b: Long = 1): Long {
        if (n == 0) {
            return a
        }

        return fibonacci(n - 1, b, a + b)
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
            toastMessage = toastReducer(actionResult),
        )
    }

    private fun pokemonDetailReducer(actionResult: PokemonDetailActionResult): Pokemon? {
        pokemon = when (actionResult) {
            is PokemonDetailActionResult.SetError -> null
            is PokemonDetailActionResult.SetPokemon -> actionResult.pokemon
            else -> pokemon
        }
        return pokemon
    }

    private fun PokemonDetailViewState.capturedSectionReducer(actionResult: PokemonDetailActionResult): CapturedSectionViewState {
        return when (actionResult) {
            is PokemonDetailActionResult.SetCapturedSectionViewState -> {
                isCaptured = actionResult.capturedSectionViewState.isCaptured

                actionResult.capturedSectionViewState
            }

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

    private fun toastReducer(actionResult: PokemonDetailActionResult): String? {
        return when (actionResult) {
            is PokemonDetailActionResult.ShowToast -> actionResult.message
            else -> null
        }
    }

    companion object {
        const val SAVED_STATE_ID = "pokemon_id"
    }
}
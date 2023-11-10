package com.adifaisalr.mypokemonapp.presentation.ui.captured

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.adifaisalr.core.domain.model.NamedApiResource
import com.adifaisalr.mypokemonapp.R
import com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist.LoadingItemView
import com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist.PokemonItemView
import com.adifaisalr.mypokemonapp.presentation.ui.util.NavigationUtils.safeNavigate

@Composable
fun CapturedPokemonListRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CapturedPokemonListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
    CapturedPokemonListScreen(
        modifier = modifier,
        viewState = viewState,
        onLoadData = {
            viewModel.loadData()
        },
        onItemClick = { item ->
            navController.safeNavigate("detail/${item.id}")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapturedPokemonListScreen(
    modifier: Modifier = Modifier,
    viewState: CapturedPokemonListViewState,
    onItemClick: (NamedApiResource) -> Unit,
    onLoadData: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        onLoadData()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.captured_pokemon),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                when {
                    viewState.isLoading && viewState.itemList.isEmpty() -> {
                        item { LoadingItemView() }
                    }

                    viewState.itemList.isEmpty() -> {
                        item {
                            Text(text = stringResource(R.string.empty_captured_pokemon))
                        }
                    }

                    else -> {
                        items(viewState.itemList.map { poke ->
                            NamedApiResource(
                                name = poke.name,
                                category = "",
                                id = poke.id,
                            )
                        }) { item ->
                            PokemonItemView(
                                searchItem = item,
                                onItemClick = onItemClick,
                            )
                        }
                    }
                }
            }
        }
    }
}
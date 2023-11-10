@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)

package com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adifaisalr.core.data.api.Api
import com.adifaisalr.core.domain.model.NamedApiResource
import com.adifaisalr.mypokemonapp.R
import com.adifaisalr.mypokemonapp.presentation.ui.util.NavigationUtils.safeNavigate
import com.adifaisalr.mypokemonapp.presentation.ui.util.OnBottomReached

@Composable
fun PokemonListRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokemonListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
    PokemonListScreen(
        modifier = modifier,
        viewState = viewState,
        onLoadNextPage = {
            viewModel.loadNextPage(false)
        },
        onRefresh = {
            viewModel.loadNextPage(true)
        },
        onItemClick = { item ->
            navController.safeNavigate("detail/${item.id}")
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewState: PokemonListViewState,
    onLoadNextPage: () -> Unit,
    onRefresh: () -> Unit = {},
    onItemClick: (NamedApiResource) -> Unit,
) {
    val listState = rememberLazyListState()
    val refreshing = viewState.isLoading && viewState.itemList.isEmpty()
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.pokemon_list),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                when {
                    viewState.isLoading && viewState.itemList.isEmpty() -> {
                        item { LoadingItemView() }
                    }

                    viewState.itemList.isEmpty() -> {
                        item {
                            Text(text = stringResource(R.string.empty_list))
                        }
                    }

                    else -> {
                        items(viewState.itemList) { searchItem ->
                            PokemonItemView(
                                searchItem = searchItem,
                                onItemClick = onItemClick,
                            )
                        }
                        if (!viewState.isLastBatch && viewState.isLoading) {
                            item { LoadingItemView() }
                        }
                    }
                }
            }
            PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
            listState.OnBottomReached(isLastPage = viewState.isLastBatch) {
                onLoadNextPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoadingItemView() {
    LoadingItemView()
}

@Composable
fun PokemonItemView(
    searchItem: NamedApiResource,
    onItemClick: (NamedApiResource) -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(searchItem) }) {
        AsyncImage(
            modifier = Modifier.width(50.dp),
            model = Api.DEFAULT_BASE_IMAGE_URL + searchItem.id + ".png",
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = ColorPainter(Color.LightGray),
            fallback = ColorPainter(Color.LightGray),
            contentDescription = "",
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = searchItem.name,
        )
    }
}

@Composable
fun LoadingItemView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
        )
    }
}
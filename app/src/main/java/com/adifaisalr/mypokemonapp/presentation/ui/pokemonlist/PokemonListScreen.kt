@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.adifaisalr.mypokemonapp.presentation.ui.util.OnBottomReached
import timber.log.Timber

@Composable
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PokemonListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val listState = rememberLazyListState()
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val onItemClick: ((NamedApiResource) -> Unit) = { item ->
//        val mediaType = MediaType.values().find { it.type == item.mediaType }
//        mediaType?.let {
//            navController.safeNavigate("mediadetail/${mediaType.id}/${item.id}")
//        }
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
                        stringResource(id = R.string.pokemon_list),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
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
                            SearchItemView(
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
            listState.OnBottomReached(isLastPage = viewState.isLastBatch) {
                viewModel.loadNextPage()
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
fun SearchItemView(
    searchItem: NamedApiResource,
    onItemClick: (NamedApiResource) -> Unit,
) {
    Log.d("wawawaw"," | ${searchItem.id}")
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
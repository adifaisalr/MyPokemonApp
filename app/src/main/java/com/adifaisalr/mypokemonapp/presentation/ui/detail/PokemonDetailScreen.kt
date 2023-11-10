package com.adifaisalr.mypokemonapp.presentation.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.adifaisalr.mypokemonapp.R

@Composable
fun PokemonDetailRoute(
    modifier: Modifier = Modifier,
    pokemonId: Int,
    navController: NavController,
    viewModel: PokemonDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val viewState by viewModel.stateFlow.collectAsStateWithLifecycle()
    viewModel.setPokemonId(pokemonId)

    PokemonDetailScreen(
        modifier = modifier,
        viewState = viewState,
        navController = navController,
        onFetchPokemonDetail = {
            viewModel.fetchPokemonDetail()
        },
        onRefresh = {
            viewModel.fetchPokemonDetail()
        },
        onClickCapture = {
            viewModel.changeCaptured()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetailScreen(
    modifier: Modifier,
    navController: NavController,
    viewState: PokemonDetailViewState,
    onFetchPokemonDetail: () -> Unit,
    onRefresh: () -> Unit,
    onClickCapture: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        onFetchPokemonDetail()
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
                        viewState.pokemon?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        PokemonDetailContent(
            modifier = modifier.padding(innerPadding),
            viewState = viewState,
            onClickCapture = onClickCapture,
        )
    }
}

@Composable
private fun PokemonDetailContent(
    modifier: Modifier,
    viewState: PokemonDetailViewState,
    onClickCapture: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            viewState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            !viewState.error.isNullOrEmpty() -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = viewState.error,
                )
            }

            else -> {
                viewState.pokemon?.let { pokemon ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(200.dp),
                            model = pokemon.sprites?.other?.officialArtwork?.frontDefault,
                            contentDescription = "pokemonSprite",
                            contentScale = ContentScale.Inside,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .weight(1f)
                                    .fillMaxWidth(),
                                text = "Height : ${pokemon.height}dm",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            val text = if (viewState.capturedSectionViewState.isCaptured) "Release"
                            else "Capture"
                            Button(
                                modifier = Modifier
                                    .wrapContentSize(),
                                enabled = !viewState.capturedSectionViewState.isLoading,
                                onClick = onClickCapture,
                            ) {
                                Text(text = text)
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_catching_pokemon_24),
                                    contentDescription = null,
                                )
                            }
                        }
                        Divider()
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            text = "Pokemon Type(s)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        pokemon.types.forEach { pokemonType ->
                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                text = "Slot ${pokemonType.slot} : ${pokemonType.type.name}",
                                fontSize = 14.sp,
                            )
                        }
                        Text(
                            modifier = Modifier.padding(
                                start = 10.dp,
                                end = 10.dp,
                                top = 20.dp,
                                bottom = 5.dp
                            ),
                            text = "Pokemon Move(s)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        pokemon.moves.forEach { move ->
                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                text = "- ${move.move.name}",
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

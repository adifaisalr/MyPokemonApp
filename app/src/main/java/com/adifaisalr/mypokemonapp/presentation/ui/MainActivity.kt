package com.adifaisalr.mypokemonapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adifaisalr.mypokemonapp.presentation.ui.detail.PokemonDetailRoute
import com.adifaisalr.mypokemonapp.presentation.ui.detail.PokemonDetailViewModel
import com.adifaisalr.mypokemonapp.presentation.ui.home.HomeScreen
import com.adifaisalr.mypokemonapp.presentation.ui.pokemonlist.PokemonListViewModel
import com.adifaisalr.mypokemonapp.presentation.ui.theme.MyPokemonAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPokemonAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("detail/{id}") { backStackEntry ->
                        val viewModel = hiltViewModel<PokemonDetailViewModel>()
                        val id = backStackEntry.arguments?.getString("id") ?: return@composable
                        PokemonDetailRoute(
                            navController = navController,
                            pokemonId = id.toInt(),
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPokemonAppTheme {
        Greeting("Android")
    }
}
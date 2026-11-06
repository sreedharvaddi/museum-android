package com.sree.museum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sree.museum.ui.nav.Destination
import com.sree.museum.ui.theme.MuseumTheme
import com.sree.museum.ui.view.MuseumItemDetailScreen
import com.sree.museum.ui.view.MuseumListScreen
import com.sree.museum.ui.view.MuseumListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val museumListViewModel: MuseumListViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onFetchClick: ()->Unit = {
            // Trigger loading
            lifecycleScope.launch {
                museumListViewModel.events.send(MuseumListViewModel.Event.LoadMuseumItems)
            }
        }

        setContent {
            MuseumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable(Destination.Home.route) {
                            HomeScreen(navController = navController, viewModel = museumListViewModel, modifier = Modifier.padding(innerPadding), onFetchClick = onFetchClick)
                        }
                        composable(Destination.Detail.route) { navBackStackEntry ->
                            val path = navBackStackEntry.arguments?.getString("path") ?: "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg"
                            MuseumItemDetailScreen(primaryImageUrl = java.net.URLDecoder.decode(path, "UTF-8"), navController = navController)
                        }
                    }
                    //HomeScreen(viewModel = museumListViewModel, modifier = Modifier.padding(innerPadding), onFetchClick = onFetchClick)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: MuseumListViewModel, modifier: Modifier = Modifier, onFetchClick: ()-> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = viewModel.state.value) {
            is MuseumListViewModel.State.Idle -> {
                Button( onClick =  onFetchClick ) {
                    Text(text = "Fetch")
                }
            }

            is MuseumListViewModel.State.Loading -> {
                CircularProgressIndicator()
            }

            is MuseumListViewModel.State.Success -> {
                MuseumListScreen(
                    navController = navController,
                    museumItems = state.museumItems,
                    modifier = modifier,
                    onFetchClick = onFetchClick
                )
            }

            is MuseumListViewModel.State.Error -> {
                Text("Error: ${state.message}")
            }
        }
    }
}
package com.sree.museum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
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

        val onFetchClick: (String)->Unit = { query ->
            // Trigger loading
            lifecycleScope.launch {
                museumListViewModel.events.send(MuseumListViewModel.Event.LoadMuseumItems(query))
            }
        }

        val onSearchClick: ()->Unit = {
            lifecycleScope.launch {
                museumListViewModel.events.send(MuseumListViewModel.Event.SearchClicked)
            }
        }

        setContent {
            MuseumTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable(Destination.Home.route) {
                        HomeScreen(
                            navController = navController, viewModel = museumListViewModel,
                            onFetchClick = onFetchClick,
                            onSearchClick = onSearchClick
                        )
                    }
                    composable(Destination.Detail.route) { navBackStackEntry ->
                        val path = navBackStackEntry.arguments?.getString("path") ?: "https://images.metmuseum.org/CRDImages/ad/original/DP208427.jpg"
                        MuseumItemDetailScreen(primaryImageUrl = java.net.URLDecoder.decode(path, "UTF-8"), navController = navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MuseumListViewModel, onFetchClick: (String)-> Unit, onSearchClick: () -> Unit = {}) {
    var isSearchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val museumItems = viewModel.museumItemsFlow.collectAsLazyPagingItems()
    val isLoading by viewModel.isLoading.collectAsState()
    Scaffold(
        floatingActionButton = {
            if (!isSearchOpen) {
                FloatingActionButton(
                    onClick = {
                        isSearchOpen = true
                        onSearchClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            //IconButton(onClick = onSearchClicked, ) { Icon(painter = painterResource(android.R.drawable.ic_menu_search), contentDescription = null) }
            //HomeScreen(viewModel = museumListViewModel, modifier = Modifier.padding(innerPadding), onFetchClick = onFetchClick)

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val state = viewModel.state.value) {
                    is MuseumListViewModel.State.Idle -> {
                        if (isSearchOpen) {
                            SearchBar(
                                inputField = {
                                    SearchBarDefaults.InputField(
                                        query = searchText,
                                        onQueryChange = { text -> searchText = text },
                                        onSearch = { submitQuery ->
                                            isSearchOpen = false
                                            onFetchClick(submitQuery)
                                        },
                                        expanded = isSearchOpen,
                                        onExpandedChange = { isSearchOpen = it },
                                        placeholder = { Text("Search") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                expanded = true,
                                onExpandedChange = { isSearchOpen = it }
                            ) {
                                MuseumListScreen(
                                    navController = navController,
                                    museumItems = museumItems,
                                    isLoadInProgress = isLoading,
                                    onSearchClicked = onSearchClick
                                )
                            }
                        } else {
                            MuseumListScreen(
                                navController = navController,
                                museumItems = museumItems,
                                isLoadInProgress = isLoading,
                                onSearchClicked = onSearchClick
                            )
                        }
                    }

                    is MuseumListViewModel.State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is MuseumListViewModel.State.Success -> {
                        MuseumListScreen(
                            navController = navController,
                            museumItems = viewModel.museumItemsFlow.collectAsLazyPagingItems(),
                            isLoadInProgress = isLoading,
                            onSearchClicked = onSearchClick
                        )
                    }

                    is MuseumListViewModel.State.Error -> {
                        Text("Error: ${state.message}")
                        Button(onClick = onSearchClick) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
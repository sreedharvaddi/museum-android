package com.sree.museum.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sree.museum.R
import com.sree.museum.domain.model.MuseumItem
import com.sree.museum.ui.nav.Destination

@Composable
fun MuseumListScreen(
    navController: NavController,
    museumItems: List<MuseumItem> = emptyList(),
    onSearchClicked: () -> Unit = {},
    isGridLayout : Boolean = false
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (museumItems.isEmpty()) {
            Image(
                painter = painterResource( R.drawable.museum_splash_screen),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(16.dp)
                    .align(Alignment.Center)
            )
        } else {
            if (isGridLayout) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(museumItems) {
                        MuseumItemCard(
                            museumItem = it,
                            onItemClicked = {
                                val route =
                                    Destination.Detail.createRoute(path = it.primaryImageUrl)
                                navController.navigate(route = route)
                            }
                        )
                    }
                }
            } else {
                val rememberedLazyListState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberedLazyListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(museumItems) {
                        MuseumItemCard(
                            museumItem = it,
                            onItemClicked = {
                                val route =
                                    Destination.Detail.createRoute(path = it.primaryImageUrl)
                                navController.navigate(route = route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MuseumItemCard(museumItem: MuseumItem, modifier: Modifier = Modifier, onItemClicked: () -> Unit = {}) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .clickable(
                onClick = onItemClicked
            )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            model = museumItem.thumbnailUrl,
            contentDescription = null
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = museumItem.id.toString())
                Text(text = museumItem.dated)
            }
            Text(text = museumItem.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MuseumListScreenPreview(navController: NavController ? = null) {
    val sampleItems = listOf(
        MuseumItem(
            id = 1,
            primaryImageUrl = "",
            thumbnailUrl = "",
            dated = "1850",
            location = "Gallery 1",
            description = "Sample Item 1"
        ),
        MuseumItem(
            id = 2,
            primaryImageUrl = "",
            thumbnailUrl = "",
            dated = "1920",
            location = "Gallery 2",
            description = "Sample Item 2"
        )
    )
    MuseumListScreen(navController = navController!!, museumItems = sampleItems)
}

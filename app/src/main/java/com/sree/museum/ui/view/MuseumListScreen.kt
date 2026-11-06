package com.sree.museum.ui.view

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sree.museum.domain.model.MuseumItem
import com.sree.museum.ui.nav.Destination

@Composable
fun MuseumListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    museumItems: List<MuseumItem> = emptyList(),
    onFetchClick: () -> Unit = {},
    onItemClicked: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(200.dp), modifier = Modifier.fillMaxSize()) {
            items(museumItems) {
                MuseumItemCard(
                    museumItem = it,
                    onItemClicked = {
                        val route = Destination.Detail.createRoute(path = it.primaryImageUrl)
                        navController.navigate(route = route)
                    }
                )
            }
        }
    }
}

@Composable
fun MuseumItemCard(museumItem: MuseumItem, modifier: Modifier = Modifier, onItemClicked: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp)
            .background(androidx.compose.ui.graphics.Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .clickable(
                onClick = onItemClicked
            )
    ) {
        AsyncImage(model = museumItem.thumbnailUrl, contentDescription = null)
        Text(text = museumItem.id.toString())
        Text(text = museumItem.dated)
        Text(text = museumItem.description)
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

package com.sree.museum.ui.view

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sree.museum.R

@Composable
fun MuseumItemDetailScreen(primaryImageUrl: String, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        PinchZoomAsyncImage(
            modifier = Modifier.fillMaxSize(),
            minScale = 1f,
            maxScale = 5f,
            model = primaryImageUrl, contentDescription = null
        )
        SmallFloatingActionButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.BottomCenter)
                .statusBarsPadding()
                .padding(bottom = 64.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back"
            )
        }
    }
}

@Composable
fun PinchZoomAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 5f
) {
    var scale by remember(model) { mutableFloatStateOf(1f) }
    var offset by remember(model) { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .clipToBounds()
            .pointerInput(model) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val oldScale = scale
                    val newScale = (scale * zoom).coerceIn(minScale, maxScale)

                    if (newScale > 1f) {
                        // Zoom centered on the pinch focal point (centroid) + pan movement
                        val zoomFactor = newScale / oldScale
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val focalPoint = centroid - center
                        offset = (offset - focalPoint) * zoomFactor + focalPoint + pan
                    } else {
                        offset = Offset.Zero
                    }
                    scale = newScale
                }
            }
            .pointerInput(model) {
                detectTapGestures(
                    onDoubleTap = { centroid ->
                        if (scale >= maxScale) {
                            // Reset to original scale and pan offset
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            // Zoom to max scale centered on the tap point
                            val newScale = maxScale
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val focalPoint = centroid - Offset(centerX, centerY)
                            offset = (Offset.Zero - focalPoint) * (newScale / 1f) + focalPoint
                            scale = newScale
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        )
    }
}
package ui

import MainViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.delay
import ui.components.PathSegments

@Composable
fun WindowScope.TopBar(
    onMinimizeWindow: () -> Unit,
    onCloseApplication: () -> Unit,
    onHandleWindowSize: () -> Unit,
    windowState: WindowState,
    uiState: MainViewModel.UiState,
    viewModel: MainViewModel
){
    LaunchedEffect(uiState.isTitleVisible){
        delay(1200)
        viewModel.hideTitle()
    }

    LaunchedEffect(uiState.currentPath){
        viewModel.generatePathSegments()
    }
    WindowDraggableArea {
        Row(
           modifier = Modifier
               .fillMaxWidth()
               .background(MaterialTheme.colorScheme.background)
        ){
            AnimatedVisibility(
                visible = uiState.isTitleVisible,
                exit = slideOutHorizontally(targetOffsetX = { -450 }, animationSpec = tween(800)) + fadeOut(animationSpec = tween(800)),
                modifier = Modifier
            ){
                Text(
                    text = "Filer",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            AnimatedVisibility(
                visible = !uiState.isTitleVisible,
                modifier = Modifier.weight(1f)
            ){
                PathSegments(
                    pathSegments = uiState.pathSegments,
                    onPathSelected = { viewModel.setCurrentPath(it.absolutePath) },
                    scrollState = rememberScrollState(),
                    modifier = Modifier
                )
            }

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                TopBarIcon(
                    onClick = { onMinimizeWindow() },
                    tooltipText = "Minimize",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Minimize,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )

                TopBarIcon(
                    onClick = { onHandleWindowSize() },
                    tooltipText = if (windowState.placement == WindowPlacement.Maximized) "Restore Down" else "Maximize",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CropSquare,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )

                TopBarIcon(
                    onClick = { onCloseApplication() },
                    tooltipText = "Close",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarIcon(
    onClick: () -> Unit,
    tooltipText: String,
    icon: @Composable () -> Unit
){
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
        tooltip = {
            PlainTooltip { Text(tooltipText, style = MaterialTheme.typography.bodySmall) }
        },
        state = rememberTooltipState()
    ){
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
        ){
            icon()
        }
    }

}
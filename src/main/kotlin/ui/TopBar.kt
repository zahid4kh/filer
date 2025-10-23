package ui

import MainViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import deskit.dialogs.confirmation.ConfirmationDialog
import ui.components.AnimatedSettingsIcon
import ui.components.PathSegments
import ui.components.SettingsDropdown

@Composable
fun WindowScope.TopBar(
    onMinimizeWindow: () -> Unit,
    onCloseApplication: () -> Unit,
    onHandleWindowSize: () -> Unit,
    windowState: WindowState,
    uiState: MainViewModel.UiState,
    viewModel: MainViewModel
){
    val minimizeIconInteractionSource = remember { MutableInteractionSource() }
    val maximizeIconInteractionSource = remember { MutableInteractionSource() }
    val closeIconInteractionSource = remember { MutableInteractionSource() }
    val settingsIconInteractionSource = remember { MutableInteractionSource() }
    val deleteIconInteractionSource = remember { MutableInteractionSource() }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.currentPath){
        viewModel.generatePathSegments()
    }

    if (showDeleteConfirmDialog) {
        ConfirmationDialog(
            width = 400.dp,
            height = 200.dp,
            title = "Delete Files",
            message = "Are you sure you want to delete ${uiState.selectedFiles.size} selected file(s)?",
            onConfirm = {
                viewModel.deleteSelectedFiles()
                showDeleteConfirmDialog = false
            },
            onCancel = {
                showDeleteConfirmDialog = false
            },
            resizable = false
        )
    }

    WindowDraggableArea {
        Row(
           modifier = Modifier
               .fillMaxWidth()
               .background(MaterialTheme.colorScheme.background)
        ){
            PathSegments(
                pathSegments = uiState.pathSegments,
                onPathSelected = { viewModel.setCurrentPath(it.absolutePath) },
                scrollState = rememberScrollState(),
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                AnimatedVisibility(
                    visible = uiState.selectedFiles.isNotEmpty(),
                    enter = scaleIn(), exit = scaleOut()
                ) {
                    TopBarIcon(
                        onClick = { showDeleteConfirmDialog = true },
                        tooltipText = "Delete selected files (${uiState.selectedFiles.size})",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null
                            )
                        },
                        interactionSource = deleteIconInteractionSource
                    )
                }

                Box {
                    TopBarIcon(
                        onClick = { viewModel.expandSettings() },
                        tooltipText = "Settings",
                        icon = { AnimatedSettingsIcon() },
                        interactionSource = settingsIconInteractionSource
                    )

                    SettingsDropdown(
                        isExpanded = uiState.isSettingsExpanded,
                        onDismiss = { viewModel.collapseSettings() },
                        offset = DpOffset(-(30).dp, 10.dp),
                        uiState = uiState,
                        onChangeTheme = { viewModel.toggleDarkMode() },
                        onShowHiddenFiles = { viewModel.handleDotFilesVisibility() }
                    )
                }


                TopBarIcon(
                    onClick = { onMinimizeWindow() },
                    tooltipText = "Minimize",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Minimize,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    },
                    interactionSource = minimizeIconInteractionSource
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
                    },
                    interactionSource = maximizeIconInteractionSource
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
                    },
                    interactionSource = closeIconInteractionSource
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
    icon: @Composable () -> Unit,
    interactionSource: MutableInteractionSource
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
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand)
                .hoverable(interactionSource)
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(
                        color = MaterialTheme.colorScheme.tertiary,
                        radius = 20.dp
                    )
                )
        ){
            icon()
        }
    }

}
package ui

import MainViewModel
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.unit.dp
import ui.components.FileInfoDialog
import ui.components.FileItem
import ui.components.FolderItem
import java.io.File


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FileListSection(
    listState: LazyListState,
    uiState: MainViewModel.UiState,
    viewModel: MainViewModel
){
    var selectedFileForInfo by remember { mutableStateOf<File?>(null) }

    selectedFileForInfo?.let { file ->
        FileInfoDialog(
            file = file,
            onClose = { selectedFileForInfo = null },
            resizable = true
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ){
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .padding(5.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize()
                .weight(1f)
        ){
            items(items = uiState.files.sorted()){ item ->
                if(File(item).isDirectory){
                    var isFolderVisible by remember { mutableStateOf(false)}
                    val scale by animateFloatAsState(
                        targetValue = if(isFolderVisible) 1f else 0f,
                    )
                    FolderItem(
                        viewModel = viewModel,
                        item = item,
                        onShowFileInfoDialog = {
                            selectedFileForInfo = File(it)
                        },
                        modifier = Modifier
                            .onFirstVisible{ isFolderVisible = !isFolderVisible }
                            .scale(scale)
                            .animateItem(placementSpec = spring())
                    )


                }else{
                    val fileInteractionSource = remember { MutableInteractionSource() }
                    val isFileHovered by fileInteractionSource.collectIsHoveredAsState()
                    var isFileVisible by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(
                        targetValue = if(isFileVisible) 1f else 0f,
                    )
                    FileItem(
                        item = item,
                        isChecked = uiState.selectedFiles.contains(item),
                        onCheckedChange = { viewModel.toggleFileSelection(item) },
                        onShowFileInfoDialog = {
                            selectedFileForInfo = File(it)
                        },
                        onOpenFile = { viewModel.openFile(item) },
                        onDeleteFile = { viewModel.deleteFile(item) },
                        interactionSource = fileInteractionSource,
                        isHovered = isFileHovered,
                        modifier = Modifier
                            .onFirstVisible{ isFileVisible = !isFileVisible }
                            .scale(scale)
                            .animateItem(placementSpec = spring()),
                        previewImage = uiState.imageForPreview,
                        viewModel = viewModel
                    )
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(listState),
            modifier = Modifier
                .padding(5.dp)
                .pointerHoverIcon(PointerIcon.Hand),
            style = LocalScrollbarStyle.current.copy(
                hoverColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unhoverColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}
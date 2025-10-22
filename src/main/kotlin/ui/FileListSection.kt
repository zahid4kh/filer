package ui

import MainViewModel
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import filer.resources.Res
import filer.resources.eye
import filer.resources.eye_off
import org.jetbrains.compose.resources.painterResource
import ui.components.FileInfoDialog
import ui.components.FileItem
import ui.components.FolderItem
import java.io.File


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
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(start = 5.dp, end = 5.dp).weight(1f)
        ){
            item{
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { viewModel.handleDotFilesVisibility() },
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                    ){
                        Icon(
                            painter = if(uiState.showDotFiles) painterResource(Res.drawable.eye) else painterResource(Res.drawable.eye_off),
                            contentDescription = null
                        )
                    }
                }
            }
            items(items = uiState.files.sorted()){ item ->
                if(File(item).isDirectory){
                    FolderItem(
                        viewModel = viewModel,
                        item = item,
                        onShowFileInfoDialog = {
                            selectedFileForInfo = File(it)
                        }
                    )
                }else{
                    val fileInteractionSource = remember { MutableInteractionSource() }
                    val isFileHovered by fileInteractionSource.collectIsHoveredAsState()
                    FileItem(
                        item = item,
                        onShowFileInfoDialog = {
                            selectedFileForInfo = File(it)
                        },
                        onOpenFile = { viewModel.openFile(item) },
                        onDeleteFile = {  },
                        interactionSource = fileInteractionSource,
                        isHovered = isFileHovered
                    )
                }
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(listState),
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .pointerHoverIcon(PointerIcon.Hand)
        )
    }
}
package ui

import MainViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.FileItem
import ui.components.FolderItem
import java.io.File


@Composable
fun FileListSection(
    listState: LazyListState,
    uiState: MainViewModel.UiState,
    viewModel: MainViewModel
){
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ){
        items(
            items = uiState.files.sorted()
        ){ item ->
            if(File(item).isDirectory){
                FolderItem(
                    viewModel = viewModel,
                    item = item
                )
            }else{
                FileItem(
                    viewModel = viewModel,
                    item = item
                )
            }
        }
    }
}

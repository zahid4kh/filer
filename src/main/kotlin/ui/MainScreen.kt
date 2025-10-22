package ui

import MainViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainViewModel,
    uiState: MainViewModel.UiState
){
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
    ){
        FileListSection(
            listState = listState,
            viewModel = viewModel,
            uiState = uiState
        )
    }
}

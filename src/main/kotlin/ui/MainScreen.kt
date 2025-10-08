package ui

import MainViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import theme.AppTheme


@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel,
    uiState: MainViewModel.UiState
) {
    AppTheme(darkTheme = uiState.darkMode) {

    }
}

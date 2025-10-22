package ui

import MainViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import theme.AppTheme


@Composable
@Preview
fun App(
    viewModel: MainViewModel,
    uiState: MainViewModel.UiState
) {
    AppTheme(darkTheme = uiState.darkMode) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationSection(
                uiState = uiState,
                onNavigateToHome = { viewModel.navigateToHome() },
                onNavigateToMusic = { viewModel.navigateToMusic() },
                onNavigateToVideos = { viewModel.navigateToVideos() },
                onNavigateToPictures = { viewModel.navigateToPictures() },
                onNavigateToDocuments = { viewModel.navigateToDocuments() },
                onNavigateToDownloads = { viewModel.navigateToDownloads() }
            )

            MainScreen(
                modifier = Modifier.weight(0.8f),
                viewModel = viewModel,
                uiState = uiState
            )
        }
    }
}

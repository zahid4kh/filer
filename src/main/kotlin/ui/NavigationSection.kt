package ui

import MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Composable
fun NavigationSection(
    uiState: MainViewModel.UiState,
    modifier: Modifier,
    onNavigateToHome: () -> Unit,
    onNavigateToDownloads: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToPictures: () -> Unit,
    onNavigateToVideos: () -> Unit,
    onNavigateToMusic: () -> Unit
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 5.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        NavItem(
            title = "Home",
            icon = Icons.Default.Home,
            onNavigateToPath = { onNavigateToHome() }
        )

        NavItem(
            title = "Downloads",
            icon = Icons.Default.Download,
            onNavigateToPath = { onNavigateToDownloads() }
        )

        NavItem(
            title = "Documents",
            icon = Icons.Default.FileOpen,
            onNavigateToPath = { onNavigateToDocuments() }
        )

        NavItem(
            title = "Music",
            icon = Icons.Default.MusicNote,
            onNavigateToPath = { onNavigateToMusic() }
        )

        NavItem(
            title = "Videos",
            icon = Icons.Default.VideoFile,
            onNavigateToPath = { onNavigateToVideos() }
        )

        NavItem(
            title = "Pictures",
            icon = Icons.Default.Image,
            onNavigateToPath = { onNavigateToPictures() }
        )
    }
}

@Composable
fun NavItem(
    title: String,
    icon: ImageVector,
    onNavigateToPath: () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = { onNavigateToPath() })
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(5.dp)
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}
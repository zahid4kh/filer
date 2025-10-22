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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp

@Composable
fun NavigationSection(
    uiState: MainViewModel.UiState,
    onNavigateToHome: () -> Unit,
    onNavigateToDownloads: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToPictures: () -> Unit,
    onNavigateToVideos: () -> Unit,
    onNavigateToMusic: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ){
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(5.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.tertiary),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NavItem(
                title = "Home",
                icon = Icons.Default.Home,
                onNavigateToPath = { onNavigateToHome() },
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .padding(top = 10.dp)
            )

            NavItem(
                title = "Downloads",
                icon = Icons.Default.Download,
                onNavigateToPath = { onNavigateToDownloads() },
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            NavItem(
                title = "Documents",
                icon = Icons.Default.FileOpen,
                onNavigateToPath = { onNavigateToDocuments() },
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            NavItem(
                title = "Music",
                icon = Icons.Default.MusicNote,
                onNavigateToPath = { onNavigateToMusic() },
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            NavItem(
                title = "Videos",
                icon = Icons.Default.VideoFile,
                onNavigateToPath = { onNavigateToVideos() },
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            NavItem(
                title = "Pictures",
                icon = Icons.Default.Image,
                onNavigateToPath = { onNavigateToPictures() },
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }

}

@Composable
fun NavItem(
    title: String,
    icon: ImageVector,
    onNavigateToPath: () -> Unit,
    modifier: Modifier
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = { onNavigateToPath() })
            .background(MaterialTheme.colorScheme.secondary)
            .padding(5.dp)
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
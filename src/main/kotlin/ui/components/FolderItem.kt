package ui.components

import MainViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun FolderItem(
    viewModel: MainViewModel,
    item: String,
    onShowFileInfoDialog: (String) -> Unit,
    modifier: Modifier
){
    val scope = rememberCoroutineScope()
    var count by remember { mutableStateOf(0) }
    LaunchedEffect(item){
        scope.launch(Dispatchers.IO) {
            count = File(item).listFiles().size
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable(
                onClick = { viewModel.setCurrentPath(dir = item) }
            ).pointerHoverIcon(PointerIcon.Hand),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
            )

            Text(
                text = File(item).name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().animateContentSize(),
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = { onShowFileInfoDialog(item) }
            ){
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ){
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(5.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}


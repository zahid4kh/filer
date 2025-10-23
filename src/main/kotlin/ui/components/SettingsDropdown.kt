package ui.components

import MainViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import filer.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsDropdown(
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    offset: DpOffset,
    uiState: MainViewModel.UiState,
    onChangeTheme: () -> Unit,
    onShowHiddenFiles: () -> Unit
){
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onDismiss() },
        offset = offset,
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ){
        DropdownMenuItem(
            text = {
                Text(
                    text = "Switch theme",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if(uiState.darkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = { onChangeTheme() },
            modifier = Modifier
                .padding(horizontal = 7.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "Show hidden files",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                Switch(
                    checked = uiState.showDotFiles,
                    onCheckedChange = { onShowHiddenFiles() },
                    thumbContent = {
                        Icon(
                            painter = if(uiState.showDotFiles) painterResource(Res.drawable.eye) else painterResource(Res.drawable.eye_off),
                            contentDescription = null,
                            modifier = Modifier.padding(4.dp)
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                        checkedBorderColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surfaceTint,
                        checkedThumbColor = MaterialTheme.colorScheme.onSurface,
                        uncheckedIconColor = MaterialTheme.colorScheme.surface,
                        checkedIconColor = MaterialTheme.colorScheme.surface,
                    )
                )
            },
            onClick = { onShowHiddenFiles() },
            modifier = Modifier
                .padding(horizontal = 7.dp)
                .clip(MaterialTheme.shapes.medium)
        )
    }
}
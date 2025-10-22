package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import calculateFolderSize
import deskit.dialogs.info.InfoDialog
import filer.resources.Res
import filer.resources.document
import formatFileSize
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Composable
internal fun FileInfoDialog(
    file: File,
    onClose: () -> Unit,
    resizable: Boolean
) {
    val fileSize = remember(file) {
        if (file.isFile) {
            formatFileSize(file.length())
        } else {
            "Folder"
        }
    }

    val lastModified = remember(file) {
        val date = Date(file.lastModified())
        SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault()).format(date)
    }

    val fileExtension = remember(file) {
        if (file.isFile && file.extension.isNotEmpty()) {
            ".${file.extension}"
        } else {
            "N/A"
        }
    }

    val folderSize = remember(file) {
        if (file.isDirectory) {
            calculateFolderSize(file)
        } else {
            0L
        }
    }

    val totalFiles = remember(file) {
        if (file.isDirectory) {
            file.listFiles()?.size ?: 0
        } else {
            0
        }
    }

    InfoDialog(
        width = 400.dp,
        height = 320.dp,
        title = "File Information",
        onClose = onClose,
        resizable = resizable
    ) {
        val scrollState = rememberScrollState()
        Box{
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // File/Folder icon and name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.document),
                        contentDescription = null,
                        tint = if (file.isDirectory) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider()

                // Metadata
                InfoRow("Type", if (file.isDirectory) "Folder" else "File")
                if (file.isFile) {
                    InfoRow("Extension", fileExtension)
                    InfoRow("Size", fileSize)
                }
                InfoRow("Location", file.parent ?: "Unknown")
                AnimatedVisibility(
                    visible = file.isDirectory
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        InfoRow("Total files", totalFiles.toString())
                        InfoRow("Folder size", formatFileSize(folderSize))
                    }
                }
                InfoRow("Modified", lastModified)
            }
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .align(Alignment.BottomEnd)
                    .pointerHoverIcon(PointerIcon.Hand),
                adapter = rememberScrollbarAdapter(scrollState),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colorScheme.outline,
                    unhoverColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InfoRow(
    label: String,
    value: String
) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val isLocationLabel = label.contentEquals("Location")
    var isCopied by remember { mutableStateOf(false) }

    val locationModifier = if(isLocationLabel){
        Modifier
            .clip(MaterialTheme.shapes.small)
            .clickable{
                clipboard.setContents(StringSelection(value), null)
                isCopied = true
            }
            .padding(5.dp)
    }else{
        Modifier
    }

    LaunchedEffect(isCopied) {
        if (isCopied) {
            delay(2000)
            isCopied = false
        }
    }

    val displayMessage = if (isCopied) "Path Copied!!!" else value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        if(isLocationLabel && value.length > 20){
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                state = rememberTooltipState(),
                tooltip = {
                    PlainTooltip {
                        Text(
                            text = if (isCopied) "Copied to clipboard!" else "Click to copy path"
                        )
                    }
                }
            ){
                Text(
                    text = displayMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCopied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = locationModifier.pointerHoverIcon(PointerIcon.Hand)
                )
            }
        }else{
            Text(
                text = displayMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCopied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = locationModifier
            )
        }
    }
}
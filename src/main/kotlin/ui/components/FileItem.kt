package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import filer.resources.Res
import filer.resources.document
import org.jetbrains.compose.resources.painterResource
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileItem(
    item: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onShowFileInfoDialog: (String) -> Unit,
    onOpenFile: () -> Unit,
    onDeleteFile: () -> Unit,
    interactionSource: MutableInteractionSource,
    isHovered: Boolean
){
    val deleteIconInteractionSource = remember { MutableInteractionSource() }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable{ onOpenFile() }
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(Res.drawable.document),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(33.dp)
                    .padding(horizontal = 5.dp, vertical = 1.dp)
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = isHovered,
                enter = scaleIn(),
                exit = scaleOut()
            ){
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Below),
                    tooltip = {
                        RichTooltip(
                            title = { Text("Delete", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium) },
                            colors = TooltipDefaults.richTooltipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text("Immediately delete the file", style = MaterialTheme.typography.bodyMedium)
                        }
                    },
                    state = rememberTooltipState()
                ){
                    IconButton(
                        onClick = { onDeleteFile() }
                    ){
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }

            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )

            IconButton(
                onClick = { onShowFileInfoDialog(item) }
            ){
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }


    }
}
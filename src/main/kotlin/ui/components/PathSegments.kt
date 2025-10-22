package ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import theme.getJetbrainsMonoFamily
import java.io.File


@Composable
internal fun PathSegments(
    pathSegments: List<File>,
    onPathSelected: (File) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(bottom = 10.dp, end = 12.dp, top = 4.dp, start = 3.dp)
        ) {
            pathSegments.forEachIndexed { index, dir ->
                Text(
                    text = dir.name.ifBlank { "." },
                    color = if (index == pathSegments.lastIndex)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable { onPathSelected(dir) }
                        .padding(6.dp)
                        .pointerHoverIcon(PointerIcon.Hand),
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = getJetbrainsMonoFamily(),
                    fontWeight = FontWeight.Bold,
                )
                if (index != pathSegments.lastIndex) {
                    Text(
                        text = "/",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getJetbrainsMonoFamily(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        HorizontalScrollbar(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 12.dp)
                .pointerHoverIcon(PointerIcon.Hand),
            adapter = rememberScrollbarAdapter(scrollState),
            style = LocalScrollbarStyle.current.copy(
                hoverColor = MaterialTheme.colorScheme.outline,
                unhoverColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}
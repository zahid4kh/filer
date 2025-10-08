@file:JvmName("Filer")
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.appModule
import theme.AppTheme
import java.awt.Dimension
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import filer.resources.*
import org.jetbrains.compose.resources.painterResource
import ui.MainScreen
import ui.TopBar


fun main() = application {
    startKoin {
        modules(appModule)
    }

    val viewModel = getKoin().get<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(800.dp, 600.dp)),
        alwaysOnTop = true,
        title = "Filer",
        undecorated = true,
        icon = painterResource(Res.drawable.file_icon)
    ) {
        window.minimumSize = Dimension(800, 600)

        AppTheme {
            Column{
                TopBar()

                MainScreen(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }

        }
    }
}
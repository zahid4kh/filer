import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel(
    private val database: Database,
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val homeProperty = System.getProperty("user.home")
    private val homeDir = File(homeProperty)

    init {
        viewModelScope.launch {
            val settings = database.getSettings()
            _uiState.value = _uiState.value.copy(
                darkMode = settings.darkMode,
            )

            initializeHomeScreen()
        }
    }

    fun setCurrentPath(dir: String){
        _uiState.update { it.copy(currentPath = dir) }
        updateListOfFiles()
    }

    fun navigateToDownloads(){
        _uiState.update { it.copy(currentPath = "${homeDir.absolutePath}/Downloads") }
        updateListOfFiles()
    }

    fun navigateToHome(){
        _uiState.update { it.copy(currentPath = homeDir.absolutePath) }
        updateListOfFiles()
    }

    fun navigateToMusic(){
        _uiState.update { it.copy(currentPath = "${homeDir.absolutePath}/Music") }
        updateListOfFiles()
    }

    fun navigateToVideos(){
        _uiState.update { it.copy(currentPath = "${homeDir.absolutePath}/Videos") }
        updateListOfFiles()
    }

    fun navigateToPictures(){
        _uiState.update { it.copy(currentPath = "${homeDir.absolutePath}/Pictures") }
        updateListOfFiles()
    }

    fun navigateToDocuments(){
        _uiState.update { it.copy(currentPath = "${homeDir.absolutePath}/Documents") }
        updateListOfFiles()
    }

    private fun updateListOfFiles(){
        _uiState.update { it.copy(
            files = File(_uiState.value.currentPath).listFiles().map { file ->
                file.absolutePath
            }
        ) }
    }

    private suspend fun initializeHomeScreen(){
        val homeFiles = homeDir.listFiles().map { file ->
            file.absolutePath
        }
        withContext(Dispatchers.Main){
            _uiState.update {
                it.copy(currentPath = homeDir.absolutePath, files = homeFiles)
            }
        }

    }

    fun toggleDarkMode() {
        val newDarkMode = !_uiState.value.darkMode
        _uiState.value = _uiState.value.copy(darkMode = newDarkMode)

        viewModelScope.launch {
            val settings = database.getSettings()
            database.saveSettings(settings.copy(darkMode = newDarkMode))
        }
    }

    data class UiState(
        val darkMode: Boolean = false,
        val currentPath: String = "",
        val files: List<String> = mutableListOf()
    )
}
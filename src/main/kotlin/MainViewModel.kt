import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
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
import java.awt.Desktop
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.collections.filter

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
                showDotFiles = settings.showDotFiles
            )

            initializeHomeScreen()
            generatePathSegments()
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
        viewModelScope.launch(Dispatchers.IO) {
            val allFiles = File(_uiState.value.currentPath).listFiles()?.map { file ->
                file.absolutePath
            } ?: emptyList()

            val filteredFiles = if (_uiState.value.showDotFiles) {
                allFiles
            } else {
                allFiles.filter { path ->
                    !File(path).name.startsWith(".")
                }
            }

            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(files = filteredFiles) }
            }
        }
    }

    private suspend fun initializeHomeScreen(){
        withContext(Dispatchers.IO) {
            val allFiles = homeDir.listFiles()?.map { file ->
                file.absolutePath
            } ?: emptyList()

            val filteredFiles = if (_uiState.value.showDotFiles) {
                allFiles
            } else {
                allFiles.filter { path ->
                    !File(path).name.startsWith(".")
                }
            }

            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(currentPath = homeDir.absolutePath, files = filteredFiles)
                }
            }
        }
    }

    fun handleDotFilesVisibility(){
        viewModelScope.launch(Dispatchers.IO) {
            val newShowDotFiles = !_uiState.value.showDotFiles

            withContext(Dispatchers.Main){
                _uiState.update { it.copy(showDotFiles = newShowDotFiles) }
            }

            val settings = database.getSettings()
            database.saveSettings(settings.copy(showDotFiles = newShowDotFiles))

            updateListOfFiles()
        }
    }

    fun generatePathSegments(){
        val pathSegments = generateSequence(File(_uiState.value.currentPath)) { it.parentFile }
            .toList()
            .asReversed()

        _uiState.update { it.copy(pathSegments = pathSegments) }
    }

    fun openFile(file: String){
        viewModelScope.launch(Dispatchers.IO) {
            val fileToOpen = File(file)
            val desktop = Desktop.getDesktop()
            desktop.open(fileToOpen)
        }
    }

    fun deleteFile(file: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileToDelete = File(file)
                fileToDelete.delete()
            }catch (e: IOException){
                println("File '$file' was not deleted")
                e.printStackTrace()
            }

            updateListOfFiles()
        }
    }

    fun toggleFileSelection(file: String) {
        _uiState.update { state ->
            val newSelection = if (state.selectedFiles.contains(file)) {
                state.selectedFiles - file
            } else {
                state.selectedFiles + file
            }
            state.copy(selectedFiles = newSelection)
        }
    }

    fun deleteSelectedFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val filesToDelete = _uiState.value.selectedFiles

            filesToDelete.forEach { filePath ->
                try {
                    File(filePath).delete()
                } catch (e: IOException) {
                    println("File '$filePath' was not deleted")
                    e.printStackTrace()
                }
            }

            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(selectedFiles = emptySet()) }
            }

            updateListOfFiles()
        }
    }

    fun expandSettings(){
        _uiState.update { it.copy(isSettingsExpanded = true) }
    }

    fun collapseSettings(){
        _uiState.update { it.copy(isSettingsExpanded = false) }
    }

    fun toggleDarkMode() {
        val newDarkMode = !_uiState.value.darkMode
        _uiState.value = _uiState.value.copy(darkMode = newDarkMode)

        viewModelScope.launch {
            val settings = database.getSettings()
            database.saveSettings(settings.copy(darkMode = newDarkMode))
        }
    }

    fun previewImage(image: String){
        val imageForPreview = File(image)

        viewModelScope.launch(Dispatchers.IO) {
            if(isImage(image)){
                val image = ImageIO.read(imageForPreview).toComposeImageBitmap()
                withContext(Dispatchers.Main){
                    _uiState.update { it.copy(imageForPreview = image) }
                }
            }else {
                _uiState.update { it.copy(imageForPreview = null) }
                return@launch
            }
        }
    }

    fun isImage(file: String): Boolean{
        val extensions = listOf("png", "jpeg", "jpg")
        val isImage = extensions.contains(File(file).extension)
        return isImage
    }

    fun resetImagePreview(){
        _uiState.update { it.copy(imageForPreview = null) }
    }

    data class UiState(
        val darkMode: Boolean = false,
        val currentPath: String = "",
        val files: List<String> = mutableListOf(),
        val showDotFiles: Boolean = false,
        val isTitleVisible: Boolean = true,
        val pathSegments: List<File> = emptyList(),
        val isSettingsExpanded: Boolean = false,
        val selectedFiles: Set<String> = emptySet(),
        val imageForPreview: ImageBitmap? = null
    )
}
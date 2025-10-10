import java.io.File

internal fun formatFileSize(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024) return "%.1f KB".format(kb)
    val mb = kb / 1024.0
    if (mb < 1024) return "%.1f MB".format(mb)
    val gb = mb / 1024.0
    return "%.1f GB".format(gb)
}

internal fun calculateFolderSize(folder: File): Long {
    var size = 0L
    try {
        folder.listFiles()?.forEach { file ->
            size += if (file.isDirectory) {
                calculateFolderSize(file)
            } else {
                file.length()
            }
        }
    } catch (e: Exception) {}
    return size
}
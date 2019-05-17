import java.io.File

fun readLines(resource: String): List<String> {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource(resource).file).forEachLine {
        lines.add(it)
    }
    return lines
}


import java.io.File

fun readFileLineByLineUsingForEachLine(fileName: String) : List<String> {
    val bufferedReader = File(fileName).bufferedReader()
    val lineList = mutableListOf<String>()

    bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }

    return lineList.toList()
}

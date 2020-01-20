import java.io.File
import java.util.*

fun main(){
    decoder("src/file.txt", "src")
}
fun decoder(base64Str: String, pathFile: String): Unit{
    val imageByteArray = Base64.getDecoder().decode(base64Str)
    File(pathFile).writeBytes(imageByteArray)
}

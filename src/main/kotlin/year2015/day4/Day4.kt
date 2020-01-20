package year2015.day4

import java.math.BigInteger
import java.security.MessageDigest

fun main() {

    val key = "ckczppom"
    var number = 0
    var i = 0
    
    while(number == 0){
        val newKey = key + i
        val md5 = newKey.md5()
        
        if(md5.take(6) == "000000"){
            number = i
            break
        }
        i++
    }
    
    println(number)

}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

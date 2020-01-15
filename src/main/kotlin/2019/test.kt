package `2019`

fun main(){
    var number =851;
    val digit = mutableListOf<Int>()

    while (number > 0) {
        digit.plusAssign(number % 10)
        number /= 10
    }

    println(digit.reverse())
}

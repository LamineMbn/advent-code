package day16

import readFileLineByLineUsingForEachLine

private val signal: CharArray = readFileLineByLineUsingForEachLine("src/day16/input_AoC_input_0.txt").map { it.toCharArray() }.first()

fun main (){
    val phase = arrayListOf(0, 1, 0 , -1)

    val input = signal.toMutableList()

    val numberOfPhase = 100
    val element = 0

    for (i in 0 until  numberOfPhase - 1){
        var sum = 0
        var offset = 1
        val p = mutableListOf<Int>()
        var k = 0

        for (j in input.indices) {
            if(k == phase.size){
                k = 0
            }
            p.plus(phase[k])
            k++
        }

        val shift = p[0]
        val ttt = p.subList(1,p.size).plusAssign(shift)
        print(p)
//        for (digit in input){
//            sum += digit.toInt() * p[i]
//        }
    }
}

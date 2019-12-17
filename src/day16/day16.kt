package day16

import readFileLineByLineUsingForEachLine
import kotlin.math.abs

private val signal: CharArray =
    readFileLineByLineUsingForEachLine("src/day16/input_AoC_input_1.txt").map { it.toCharArray() }.first()

private val phase = arrayListOf(0, 1, 0, -1)

fun main() {


    var input = signal.map { Integer.valueOf(it.toString()) }.toMutableList()
    var outputSignal: MutableList<Int>

    val numberOfPhase = 100

    for (i in 1 until numberOfPhase + 1) {
        outputSignal = input
        for (j in 0 until outputSignal.size) {
            var sumPerSignal = 0
            val phases = shiftList(outputSignal, j + 1)
            for (k in 0 until outputSignal.size) {
                val value = phases[k].times(outputSignal[k])
                sumPerSignal += value

            }
            input[j] = abs(sumPerSignal) % 10
        }
    }

    input.take(8).forEach { print(it) }

}

private fun shiftList(
    input: MutableList<Int>,
    offSet: Int
): List<Int> {
    val p = mutableListOf<Int>()

    var k = 0

    loop@ for (i in input.indices + 1) {
        for (j in 0 until offSet) {
            if (p.size == input.size + 1) {
                break@loop
            }
            p += phase[k]
        }
        if (k == phase.size - 1) {
            k = 0
        } else {
            k++
        }
    }

//    p.forEach { print(it) }
//    println()
//    p.subList(1, p.size).forEach { print(it) }

    return p.subList(1, p.size)
}

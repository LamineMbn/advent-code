package year2019.day16

import readFile
import java.util.*
import kotlin.math.abs

private val signal: CharArray =
    readFile("src/main/kotlin/day16/input_AoC_input_1.txt").map { it.toCharArray() }.first()

private val phase = arrayListOf(0, 1, 0, -1)

fun main() {

    var input = signal.map { Integer.valueOf(it.toString()) }.toMutableList()

    val numberOfPhase = 100

    println("Size = ${input.size}")

    calculateFFT(numberOfPhase, input, retrievePhaseList(input))

    val hundredthInput = input.take(8).joinToString("").toInt()
//    check(hundredthInput == 10189359)
    println("The 100th input is = $hundredthInput")

    var inputPart2 = signal.map { Integer.valueOf(it.toString()) }.toMutableList()
    var finalSignal = Collections.nCopies(10000, inputPart2).flatten().toMutableList()

    val offSet = inputPart2.take(7).joinToString("").toInt()
    println("The offset is = $offSet")
    println("Size = ${finalSignal.size}")


    check(finalSignal.size == 10000 * inputPart2.size)
    repeat(numberOfPhase){
        finalSignal.indices.reversed().fold(0) {signal, i -> (abs(signal + finalSignal[i]) % 10).also { finalSignal[i] = it }}
    }


    val message = finalSignal.subList(offSet, finalSignal.size).take(8).joinToString("").toInt()
    println("The final message is = $message")

}

private fun calculateFFT(
    numberOfPhase: Int,
    input: MutableList<Int>,
    phaseForN : MutableList<List<Int>>
) {
    var outputSignal: MutableList<Int>

    repeat(numberOfPhase) {
        outputSignal = input
        for (j in 0 until outputSignal.size) {
            var sumPerSignal = 0
            val phases = phaseForN[j]
            for (k in 0 until outputSignal.size) {
                val value = phases[k].times(outputSignal[k])
                sumPerSignal += value

            }
            input[j] = abs(sumPerSignal) % 10
        }
    }

}

private fun retrievePhaseList(size : Int, limit : Int): MutableList<Int> {
    val phase = IntArray(size) {0}
    for(i in size downTo limit) {
        phase[i] = 1
    }

    return phase.toMutableList()
}

private fun retrievePhaseList(input: MutableList<Int>): MutableList<List<Int>> {
    val phasesForN = mutableListOf<List<Int>>()
    for (j in 0 until input.size) {
        val phase = shiftList(input, j + 1)
        if(j == input.size/ 2){
            println("For j =$j : ")
            println(phase)
            println(phase.filter { it == 1 }.sum())
        }

        phasesForN.plusAssign(phase)
    }

    return phasesForN;
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

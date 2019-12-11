package day7

import correctCode

//private val amplifierControllerSoftware: List<Long> = readFileLineByLineUsingForEachLine("src/day7/input_AoC_input_1.txt").flatMap{ it.split(",") }.map { it.toLong() }
private val amplifierControllerSoftware: List<Long> = mutableListOf(
    3,
    26,
    1001,
    26,
    -4,
    26,
    3,
    27,
    1002,
    27,
    2,
    27,
    1,
    27,
    26,
    27,
    4,
    27,
    1001,
    28,
    -1,
    28,
    1005,
    28,
    6,
    99,
    0,
    0,
    5
)

fun main() {
    val combinaison = listOf(5, 6, 7, 8, 9)
    val settings = permute(combinaison)
    var thruster = mutableListOf<Long>()
    var output: Pair<MutableList<Long>, MutableList<Long>> = Pair(mutableListOf(), mutableListOf())
    var signal = 0L
    var inputs = amplifierControllerSoftware.toMutableList()
    var amplifiers = mutableListOf<Amplifier>()
    println(inputs)
    for (perm in settings) {
        for (case in perm) {
            amplifiers.plusAssign(Amplifier(amplifierControllerSoftware.toMutableList(), case.toLong()))
        }

        loop@for (amplifer in amplifiers) {
            amplifer.signal = signal
            try {
                output = amplifer.runProgram()
                println(amplifer.input)
            } catch (ex : RuntimeException) {
                println(ex.message)
                thruster.plusAssign(output.first)
                signal = 0L
                amplifiers = mutableListOf()
                break@loop
            }
        }
    }

//    output = correctCode(case.toLong() to signal, inputs)
//
//    println("setting = $case, signal = $signal, output = ${output.first}")
//    println(inputs)
//    signal = output.first[0]
//    inputs = amplifierControllerSoftware.toMutableList()

    println(thruster.max())
}

private fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

data class Amplifier(val input: MutableList<Long>, val phase: Long) {

    var signal: Long = 0

    fun runProgram(): Pair<MutableList<Long>, MutableList<Long>> {
        return correctCode(phase to signal, input = input)
    }

}

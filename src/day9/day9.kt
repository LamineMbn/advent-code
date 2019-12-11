package day9

import correctCode
import readFileLineByLineUsingForEachLine

private val boostProgram: List<Long> = readFileLineByLineUsingForEachLine("src/day9/input_AoC_input_1.txt").flatMap{ it.split(",") }.map { it.toLong() }

fun main() {

    val input: MutableList<Long> = boostProgram.toMutableList()

    for (i in input.size until input.size + 200){
        input.add(i,0)
    }

    val output = correctCode(2L to 0L,input.toMutableList())

    println(output.first)
    println(output.second)

}

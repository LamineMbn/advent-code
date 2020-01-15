package main.kotlin.day9

import `2019`.correctCode
import readFile

private val boostProgram: List<Long> = readFile("src/main/kotlin/2019/day9/input_AoC_input_1.txt")
    .flatMap{ it.split(",") }.map { it.toLong() }

fun main() {

    val input: MutableList<Long> = boostProgram.toMutableList()

    for (i in input.size until input.size + 200){
        input.add(i,0)
    }

    val output = correctCode(2L to 0L, input.toMutableList())

    println(output.first)
    println(output.second)

}

package main.kotlin.day1

import readFileLineByLine

private val directions: List<String> =
    readFileLineByLine("src/main/kotlin/2015/day1/input_AoC_input_1.txt").map { it.split("") }.flatten()

fun main() {
    var floor = 0
    var index = 0

    val up = directions.filter { it == "(" }.count()
    val down = directions.filter { it == ")" }.count()

    println("Floor = ${up - down}")

    for (direction in directions) {
        
        if (direction == "(") {
            floor++
        } else if (direction == ")") {
            floor--
        }

        if (floor == -1) {
            break
        }

        index++

    }

    println("Index = ${index}")
}

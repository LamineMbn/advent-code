package main.kotlin.day6

import readFileLineByLine
import kotlin.math.max

private val instructions: List<String> =
    readFileLineByLine("src/main/kotlin/2015/day6/input_Aoc_1.txt")

fun main() {

    val grid = Array(1000) { IntArray(1000) }
    
    val myInstructions = mutableListOf<Triple<Switch, Pair<Int, Int>, Pair<Int, Int>>>()

    instructions.forEach {
        run {
            val operation = it.partition { it.isLetter() }
            val coordinates = operation.second.trim().split("  ")
            val startingPoint = retrieveCoordinateFromString(coordinates[0])
            val endingPoint = retrieveCoordinateFromString(coordinates[1])
            val operationName = Switch.getEnumFromName(operation.first.dropLast(7))
            myInstructions.plusAssign(Triple(operationName, startingPoint, endingPoint))
        }
    }
    
    myInstructions.forEach { 
        for(i in it.second.first..it.third.first){
            for (j in it.second.second..it.third.second){
//                println("i = $i, j = $j")
                grid[i][j] = it.first.operation(grid[i][j])
            }
        }
    }
    
    println(grid.flatMap { row -> row.filter { true } }.sum())
}

private fun retrieveCoordinateFromString(firstCoordinate: String) = firstCoordinate.substring(
    0,
    firstCoordinate.indexOf(',')
).toInt() to firstCoordinate.substring(firstCoordinate.indexOf(',') + 1).toInt()

private enum class Switch(val switch: String) {
    TURN_ON("turnon") {
        override fun operation(value: Int): Int {
            return value + 1
        }
    },
    TURN_OFF("turnoff") {
        override fun operation(value: Int): Int {
            return max(0, value - 1)
        }
    },
    TOGGLE("toggle") {
        override fun operation(value: Int): Int {
            return value + 2
        }
    };

    abstract fun operation(value: Int): Int

    companion object {
        fun getEnumFromName(name: String): Switch {
            return Switch.values().first { it.switch == name.trim() }
        }
    }
}


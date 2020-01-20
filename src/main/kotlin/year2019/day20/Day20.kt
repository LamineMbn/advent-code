package `2019`.day20

import year2019.day10.Coordinate
import year2019.day10.x
import year2019.day10.y
import readFileLineByLine

private const val PATH = '.'
private const val WALL = '#'

private val maze: List<CharArray> =
    readFileLineByLine("src/main/kotlin/day20/input_AoC_input_0.txt").map { it.toCharArray() }

private val path = maze.withIndex().flatMap { (y, row) ->
    run {
        row.withIndex().filter { isPath(it.value) }.map { y to it.index }
    }
}

private val letters = maze.withIndex().flatMap { (y, row) ->
    run {
        row.withIndex().filter { it.value.isLetter() }.map { y to it.index }
    }
}

private val walls = maze.withIndex().flatMap { (y, row) ->
    run {
        row.withIndex().filter { isWall(it.value) }.map { y to it.index }
    }
}

private val WIDTH = maze.map { it.size }.max() ?: maze[0].size
private val HEIGHT = maze.size

fun main() {


//    maze.forEach { it.forEach { it2 ->println(it2) } }

    val gates = mutableListOf<Pair<String, Coordinate>>()

    for (i in 0 until HEIGHT) {
        for (j in 0 until WIDTH) {
            if (isLetter(maze[i][j])) {
                val gate = retrieveGate(i to j, maze)
                if (gate.first != "FOUND") {
                    gates.plusAssign(gate)
                }

            }
        }
    }

    println(path)
    println(walls)

    val entryPoint = gates.find { it.first == "AA" }
    val exitPoint = gates.find { it.first == "ZZ" }

    val passages = gates.filter { it != entryPoint && it != exitPoint }.groupBy({ it.first }, { it.second })
        .map { Passage(it.key, it.value[0], it.value[1]) }
    
    println(entryPoint)
    println(exitPoint)
    println(passages)

}

fun retrieveGate(position: Coordinate, maze: List<CharArray>): Pair<String, Coordinate> {
    val gateName = retrieveGateName(position, maze)

    if (gateName.length == 1) {
        return "FOUND" to (-1 to -1)
    }

    val gate = checkGateSurroundings(position)

    return gateName to gate
}

private fun retrieveGateName(
    position: Coordinate,
    maze: List<CharArray>
): String {
    var gateName = maze[position.x][position.y].toString()

    val x = position.x
    val y = position.y

    for (surrounding in listOf(x + 1 to y, x to y + 1)) {
        if (surrounding in letters) {
            gateName += maze[surrounding.x][surrounding.y]
        }
    }
    return gateName
}


fun checkGateSurroundings(position: Coordinate): Coordinate {
    var gate = position.x to position.y

    val x = position.x
    val y = position.y

    for (pos in listOf(x + 2 to y, x - 1 to y, x to y + 2, x to y - 1)) {
        if (pos in path) {
            gate = pos
        }
    }

    return gate
}

fun checkSurroundings(position: Coordinate, maze: List<CharArray>): Pair<Int, Int> {
    var index = position.x to position.y
    when {
        isWall(maze[position.x + 1][position.y]) -> {
            index = position.x to position.y + 1
        }
        isWall(maze[position.x][position.y + 1]) -> {
            index = position.x + 1 to position.y
        }
    }

    return index
}


fun isLetter(element: Char): Boolean = Regex("[A-Z]").containsMatchIn(element.toString())
fun isPath(element: Char): Boolean = element == PATH
fun isWall(element: Char): Boolean = element == WALL

private data class Passage(val name: String, val entrance: Coordinate, val exit: Coordinate)

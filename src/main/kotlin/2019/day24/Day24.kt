package `2019`.day24

import main.kotlin.day10.Coordinate
import main.kotlin.day10.x
import main.kotlin.day10.y
import readFile
import kotlin.math.pow

private val eris = readFile("src/`2019`.main/kotlin/day24/input_AoC_input_1.txt")
    .map { it.toCharArray() }

const val BUGS = '#'
const val EMPTY_SPACE = '.'

fun main() {

    val start: List<Coordinate> = eris.withIndex().flatMap { (y, row) ->
        run {
            row.withIndex().filter { it.value == BUGS }.map { y to it.index }
        }
    }

    val height = 5
    val width = 5

    var tmpLand = eris.map { it.copyOf() }

    for (i in 0 until height) {
        for (j in 0 until width) {
            tmpLand[i][j] = '.'
        }
    }


    var land = eris.map { it.copyOf() }

    val bugState = mutableSetOf<List<Coordinate>>()
    var bugs = start

    while (bugState.add(bugs)) {
        for (i in 0 until height) {
            for (j in 0 until width) {
                val cell = land[i][j]
                var newCell = '.'
                if (cell == BUGS) {
                    newCell = if (bugDied(
                            land,
                            i to j
                        )
                    ) EMPTY_SPACE else BUGS
                }

                if (cell == EMPTY_SPACE) {
                    newCell = if (newBug(
                            land,
                            i to j
                        )
                    ) BUGS else EMPTY_SPACE
                }

                tmpLand[i][j] = newCell

            }
        }
        bugs = tmpLand.withIndex().flatMap { (y, row) ->
            run {
                row.withIndex().filter { it.value == BUGS }.map { y to it.index }
            }
        }
        land = tmpLand.map { it.copyOf() }
    }

    println(bugs)

    val biodiversity = bugs.map { retrievePosition(it) }.map {
        calculateBiodiversity(
            it
        )
    }.sum()

    println("Biodiversity = $biodiversity")

    println()

    for (i in 0 until height) {
        for (j in 0 until width) {
            print(land[i][j])
        }
        println()
    }

}

private fun retrievePosition(point: Coordinate) = (point.x * 5) + (point.y)

private fun calculateBiodiversity(limit: Int) = 2.0.pow(limit).toInt()

private fun bugDied(land: List<CharArray>, cell: Pair<Int, Int>): Boolean {
    var bugAround = checkSurrounding(cell, land)

    return bugAround != 1
}

private fun newBug(land: List<CharArray>, cell: Pair<Int, Int>): Boolean {
    var bugAround = checkSurrounding(cell, land)

    return bugAround == 1 || bugAround == 2
}

private fun checkSurrounding(
    cell: Pair<Int, Int>,
    land: List<CharArray>
): Int {
    val i = cell.first
    val j = cell.second
    var bugAround = 0

    if (i > 0) {
        bugAround += incrementIfBugThere(land[i - 1][j])
    }
    if (j > 0) {
        bugAround += incrementIfBugThere(land[i][j - 1])
    }
    if (i < 4) {
        bugAround += incrementIfBugThere(land[i + 1][j])
    }
    if (j < 4) {
        bugAround += incrementIfBugThere(land[i][j + 1])
    }
    return bugAround
}

private fun incrementIfBugThere(
    land: Char
) = if (land == BUGS) 1 else 0

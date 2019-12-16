package day11

import paint
import readFileLineByLineUsingForEachLine

private val robotProgram: List<Long> = readFileLineByLineUsingForEachLine("src/day11/input_AoC_input_1.txt").flatMap{ it.split(",") }.map { it.toLong() }


fun main(){

    val input: MutableList<Long> = robotProgram.toMutableList()

    for (i in input.size until input.size + 1000){
        input.add(i,0)
    }

    val output = paint(0L, input)

    var passage = mutableListOf<Long>()
    var painting = mutableListOf<Long>()

    for (i in  0 until output.first.size){
        val currentElement = output.first[i]
        if(i % 2 == 0){
            painting.plusAssign(currentElement)
        } else {
            passage.plusAssign(currentElement)
        }
    }

    var coordinates = mutableListOf<Coordinate>()

    var x = 0
    var y = 0
    var dir = 0

    for (i in  0 until passage.size){
        if (passage[i] == 0L) { // move left
            dir = (dir+7)%4
        }
        if (passage[i] == 1L) { // move right
            dir = (dir+1)%4
        }
        when(dir) {
            0 -> y++
            1 -> x++
            2 -> y--
            3 -> x--
        }
        coordinates.plusAssign(Coordinate(x,y))
    }

    println(passage.size)
    println(painting.size)
    println(coordinates)
    println(coordinates.distinct())
    println(coordinates.distinct().size)
    println(coordinates.size)

    // 788

    // answer 1771

}

typealias Coordinate = Pair<Int, Int>

inline val Coordinate.x: Int get() = first
inline val Coordinate.y: Int get() = second

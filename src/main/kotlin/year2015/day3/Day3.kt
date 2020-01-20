package year2015.day3

import readFileLineByLine

private val directions: CharArray =
    readFileLineByLine("src/main/kotlin/2015/day3/input_Aoc_1.txt").map { it.toCharArray() }.first()

fun main(){
    var pos = 0 to 0
    var houses = mutableSetOf(pos)
    
    directions.forEach { run { 
        pos = retrievePositionByDirection(it, pos)
        houses.plusAssign(pos)
        
    } }
    
    println("House = ${houses.size}")

    var posSanta = 0 to 0
    var posRSanta = 0 to 0
    houses = mutableSetOf(posSanta)
    
    for (i in directions.indices){
        if(i == 0 || i % 2 == 0){
            posSanta = retrievePositionByDirection(directions[i], posSanta)
            houses.plusAssign(posSanta)
        } else {
            posRSanta = retrievePositionByDirection(directions[i], posRSanta)
            houses.plusAssign(posRSanta)
        }
    }

    println("House = ${houses.size}")
}

private fun retrievePositionByDirection(
    it: Char,
    pos: Pair<Int, Int>
): Pair<Int, Int> {
    var pos1 = pos
    when (it) {
        '^' -> pos1 = pos1.first to pos1.second + 1
        'v' -> pos1 = pos1.first to pos1.second - 1
        '>' -> pos1 = pos1.first + 1 to pos1.second
        '<' -> pos1 = pos1.first - 1 to pos1.second
    }
    return pos1
}

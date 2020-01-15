package main.kotlin.day13

import main.kotlin.day10.Coordinate
import `2019`.play
import readFile

private val arcadeGame: List<Long> = readFile("src/day13/input_AoC_input_1.txt")
    .flatMap{ it.split(",") }.map { it.toLong() }


fun main() {

//    val input: MutableList<Long> = arcadeGame.toMutableList()
//
//    addZeros(input)
//
//    val output : MutableList<Pair<Coordinate, Int>> = `2019`.play(0, input)
//
////    displayGame(output)
//
//    val numberOfBlock = output.map { it.second }.filter { it == BLOCK.value }.count()
//
//    check(numberOfBlock == 372)
//    println("Number of blocks = $numberOfBlock")



    val freeGameInput: MutableList<Long> = arcadeGame.toMutableList()
    addZeros(freeGameInput, 10000)
    putCoin(freeGameInput)

    val freeGame = play(0, freeGameInput)

    val score  = freeGame.last { it.first == Coordinate(-1, 0) }.second
    check(score == 19297)
    println("Score = $score")

}

private fun addZeros(freeGameInput: MutableList<Long>, size : Int = 1000) {
    for (i in freeGameInput.size until freeGameInput.size + size) {
        freeGameInput.add(i, 0)
    }
}

private fun putCoin(input : MutableList<Long>){
    input[0] = 2
}

enum class Tile(val value: Int){
    EMPTY(0), WALL(1), BLOCK(2), PADDLE(3), BALL(4)
}

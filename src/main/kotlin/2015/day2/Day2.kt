package main.kotlin.day2

import main.kotlin.correctCode
import readFile
import readFileLineByLine

private val dimensions: List<String> =
    readFileLineByLine("src/main/kotlin/2015/day2/input_Aoc_1.txt")

fun main(){

    val sum = dimensions.map { run { 
        val dimension = it.split("x").map { it.toInt() }
        val side1 = dimension[0] * dimension[1]
        val side2 = dimension[1] * dimension[2]
        val side3 = dimension[2] * dimension[0]
        calculateSurface(side1, side2, side3)
    } }.sum()
    
    check(sum == 1606483)
    println("Sum = $sum")

    val sumRibon = dimensions.map { run {
        val dimension = it.split("x").map { it.toInt() }
        calculateRibbon(dimension[0], dimension[1], dimension[2]) + calculateBow(dimension[0], dimension[1], dimension[2])
    } }.sum()

    println("Sum Ribon = $sumRibon")
}


fun calculateRibbon(side1 : Int, side2 : Int, side3 : Int): Int {
    val sides = orderSideBySize(side1,side2,side3)
    return 2 * sides[0] + 2 * sides[1]
}

fun calculateSurface(side1 : Int, side2 : Int, side3 : Int): Int {
    val smallestSide = orderSideBySize(side1,side2,side3).first()
    return (2*side1 + 2*side2 + 2*side3) + smallestSide
} 

fun orderSideBySize(vararg side : Int): List<Int> {
    return side.sorted()
}

fun calculateBow(l : Int, w : Int, h : Int) = l * w * h

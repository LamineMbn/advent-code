package main.kotlin.day10

import readFile
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

private val input = readFile("src/day10/input_AoC_input_1.txt")
    .map { it.toCharArray() }

fun main() {

    val asteroids: List<Coordinate> = input.withIndex().flatMap { (y, row) ->
        run {
            row.withIndex().filter { it.value == '#' }.map { it.index to y }
        }
    }

    var coordinate: MutableList<Pair<Int, Coordinate>> = mutableListOf()

    asteroids.map { asteroid ->
        run {
            coordinate.plusAssign(asteroids.filter { target -> target != asteroid }
                .map { target -> atan2Diff(asteroid, target) }.distinct().count() to asteroid)
        }
    }

    val numberOfOverlookedAst = coordinate.maxBy { it.first }?.first
    val overlookPosition: Coordinate = coordinate.maxBy { it.first }!!.second

    check(numberOfOverlookedAst == 340)
    println("$numberOfOverlookedAst overlooked Asteroids from position $overlookPosition")

    val numberOfAsteroids = asteroids.filter { it != overlookPosition }.toMutableList().size

    val targetAsteroids : Map<BigDecimal,List<Coordinate>> = asteroids.filter { it != overlookPosition }
        .sortedBy { abs(((PI)/2).minus(atan2Diff(overlookPosition, it))).toBigDecimal(MathContext.DECIMAL64) }
        .sortedBy { (((PI)/2).minus(atan2Diff(overlookPosition, it))).toBigDecimal(MathContext.DECIMAL64) > BigDecimal(0) }
        .groupBy {  (((PI)/2).minus(atan2Diff(overlookPosition, it))).toBigDecimal(MathContext.DECIMAL64) }

    val positiveTour = targetAsteroids.filter { it.key >= BigDecimal(0) }
    val negativeTour = targetAsteroids.filter { it.key < BigDecimal(0) }

    val mergeMap = (positiveTour.plus(negativeTour.toSortedMap()))

    val mutableTarget = mutableListOf<Coordinate>()

    var i = 0
    val index = 200

    while (mutableTarget.size < numberOfAsteroids){
        for (ast in mergeMap.keys){

            val asteroidsByAngle = mergeMap.getValue(ast).sortedBy {
                radius(
                    (it.x - overlookPosition.x).toDouble(),
                    (it.y - overlookPosition.y).toDouble()
                )
            }

            if (i < asteroidsByAngle.size) {
                mutableTarget.plusAssign(asteroidsByAngle[i])
            }
        }
        i++
    }

    val lastShot = (mutableTarget[index-1].x * 100) + mutableTarget[index-1].y
    check(lastShot == 2628)
    println("*** Coordinates of the 200th asteroid shot : $lastShot")
    println(mutableTarget[index-1])

}

private fun atan2Diff(asteroid: Coordinate, target: Coordinate) =
        atan2((asteroid.y - target.y).toDouble(), (target.x - asteroid.x).toDouble())

fun radius(x: Double, y: Double) : BigDecimal {
    return hypot(x , y).toBigDecimal(MathContext.DECIMAL64)
}

typealias Coordinate = Pair<Int, Int>

inline val Coordinate.x: Int get() = first
inline val Coordinate.y: Int get() = second

package main.kotlin.day12

import kotlin.math.abs


fun main() {

    var ioPos = Moon(
        Coordinate(17, -12, 13),
        Coordinate(0, 0, 0)
    )
    var europaPos = Moon(
        Coordinate(2, 1, 1),
        Coordinate(0, 0, 0)
    )
    var ganymedePos = Moon(
        Coordinate(-1, -17, 7),
        Coordinate(0, 0, 0)
    )
    var callistoPos = Moon(
        Coordinate(12, -14, 18),
        Coordinate(0, 0, 0)
    )

    val numberOfSteps = 1000

    var moons = mutableListOf(ioPos, europaPos, ganymedePos, callistoPos)


    for (step in 0 until numberOfSteps + 1) {
        println("After $step step")
        if (step > 0) {
            for (i in 0 until moons.size) {
                val currentMoon = moons[i]
                for (j in i + 1 until moons.size) {
                    val moonToCompareTo = moons[j]
                    val offset = retrieveVelocityOffset(
                        currentMoon.position,
                        moonToCompareTo.position
                    )
                    currentMoon.velocity =
                        updateVelocityOfCurent(currentMoon, offset)
                    moonToCompareTo.velocity =
                        updateVelocityOfComparable(moonToCompareTo, offset)
                }
            }
        }
        moons.forEach {
            run {
                it.updatePosition()
                println("Position = ${it.position} Velocity = ${it.velocity}")
            }
        }
    }

    println("Total Energy = ${moons.map { it.totalEnergy() }.sum()}")


    val cyclesX = mutableMapOf<Pair<State, State>, Int>()
    val cyclesY = mutableMapOf<Pair<State, State>, Int>()
    val cyclesZ = mutableMapOf<Pair<State, State>, Int>()
//    val previousStates = State(ioPos.position, europaPos.position, ganymedePos.position, callistoPos.position)

    val previousPoXState = mutableListOf(
        createState(
            retrieveXPosition(
                moons
            )
        )
    )
    val previousVelocityXState = mutableListOf(
        createState(
            retrieveXVelocity(
                moons
            )
        )
    )

    val previousPoYState = mutableListOf(
        createState(
            retrieveYPosition(
                moons
            )
        )
    )
    val previousVelocityYState = mutableListOf(
        createState(
            retrieveYVelocity(
                moons
            )
        )
    )

    val previousPoZState = mutableListOf(
        createState(
            retrieveZPosition(
                moons
            )
        )
    )
    val previousVelocityZState = mutableListOf(
        createState(
            retrieveZVelocity(
                moons
            )
        )
    )


    var initialPos = false
    var stepNeeded = 0
    var stepNeededX = 0
    var stepNeededY = 0
    var stepNeededZ = 0

    while (!initialPos) {
//        println("After ${stepNeeded} step")
        for (i in 0 until moons.size) {
            val currentMoon = moons[i]
            for (j in i + 1 until moons.size) {
                val moonToCompareTo = moons[j]
                val offset = retrieveVelocityOffset(
                    currentMoon.position,
                    moonToCompareTo.position
                )
                currentMoon.velocity = updateVelocityOfCurent(currentMoon, offset)
                moonToCompareTo.velocity =
                    updateVelocityOfComparable(moonToCompareTo, offset)
            }
        }

        stepNeeded++

        moons.forEach {
            run {
                it.updatePosition()
            }
        }

        val currentPositionXState = createState(
            retrieveXPosition(moons)
        )
        val currentVelocityXState = createState(
            retrieveXVelocity(moons)
        )

        val currentPositionYState = createState(
            retrieveYPosition(moons)
        )
        val currentVelocityYState = createState(
            retrieveYVelocity(moons)
        )

        val currentPositionZState = createState(
            retrieveZPosition(moons)
        )
        val currentVelocityZState = createState(
            retrieveZVelocity(moons)
        )

        if(stepNeededX == 0 && previousPoXState.contains(currentPositionXState) && previousVelocityXState.contains(currentVelocityXState)){

            cyclesX.putIfAbsent(currentPositionXState to currentVelocityXState, stepNeeded)
            stepNeededX = stepNeeded
            println(cyclesX)

        }

        if(stepNeededY == 0 && previousPoYState.contains(currentPositionYState) && previousVelocityYState.contains(currentVelocityYState)){

            cyclesY.putIfAbsent(currentPositionYState to currentVelocityYState, stepNeeded)
            stepNeededY = stepNeeded
            println(cyclesY)

        }

        if(stepNeededZ == 0 && previousPoZState.contains(currentPositionZState) && previousVelocityZState.contains(currentVelocityZState)){

            cyclesZ.putIfAbsent(currentPositionZState to currentVelocityZState, stepNeeded)
            stepNeededZ = stepNeeded
            println(cyclesZ)

        }

//            previousPoXState.plusAssign(currentPositionXState)
//            previousVelocityXState.plusAssign(currentVelocityXState)
//
//            previousPoYState.plusAssign(currentPositionYState)
//            previousVelocityYState.plusAssign(currentVelocityYState)
//
//            previousPoZState.plusAssign(currentPositionZState)
//            previousVelocityZState.plusAssign(currentVelocityZState)



        if (stepNeededX != 0 && stepNeededY != 0 && stepNeededZ !=0) {
            initialPos = true
        }

    }

    println(
        calcLeastCommonMultiple(
            stepNeededX.toLong(),
            stepNeededY.toLong(),
            stepNeededZ.toLong()
        )
    )
    println("Steps Needed ${calculateMiddleCycle(
        listOf(
            stepNeededX,
            stepNeededY,
            stepNeededZ
        )
    )}")


}

private fun calcLeastCommonMultiple(vararg values: Long): Long {
    var `val` = values[0]
    for (i in 1 until values.size) {
        `val` = `val` * values[i] / calcGreatestCommonDivisor(`val`, values[i])
    }
    return `val`
}

private fun calcGreatestCommonDivisor(x: Long, y: Long): Long {
    return if (x == 0L || y == 0L) {
        x + y
    } else {
        val biggerValue = Math.max(x, y)
        val smallerValue = Math.min(x, y)
        calcGreatestCommonDivisor(biggerValue % smallerValue, smallerValue)
    }
}

private fun retrieveZVelocity(moons: MutableList<Moon>) =
    moons.map { it.velocity.z }.toMutableList()

private fun retrieveZPosition(moons: MutableList<Moon>) =
    moons.map { it.position.z }.toMutableList()

private fun retrieveYVelocity(moons: MutableList<Moon>) =
    moons.map { it.velocity.y }.toMutableList()

private fun retrieveYPosition(moons: MutableList<Moon>) =
    moons.map { it.position.y }.toMutableList()

private fun retrieveXVelocity(moons: MutableList<Moon>) =
    moons.map { it.velocity.x }.toMutableList()

private fun retrieveXPosition(moons: MutableList<Moon>) =
    moons.map { it.position.x }.toMutableList()

private fun createState(list : MutableList<Int>) =
    State(list[0], list[1], list[2], list[3])

fun calculateMiddleCycle(values: List<Int>) = lcm(values)

fun gcd(x: Int, y: Int): Int {
    return if (y == 0) x else gcd(y, x % y)
}

//fun gcd(numbers: List<Int>): Int {
//    return Arrays.stream(numbers).reduce(0) { x: Int, y: Int -> gcd(x, y) }
//}

fun lcm(numbers: List<Int>): Int {
    return numbers.stream().reduce(1) { x: Int, y: Int -> x * (y / gcd(x, y)) }
}


private fun updateVelocityOfComparable(moonToCompareTo: Moon, offset: Coordinate) =
    Coordinate(
        moonToCompareTo.velocity.x - offset.x,
        moonToCompareTo.velocity.y - offset.y,
        moonToCompareTo.velocity.z - offset.z
    )

private fun updateVelocityOfCurent(currentMoon: Moon, offset: Coordinate) =
    Coordinate(
        currentMoon.velocity.x + offset.x,
        currentMoon.velocity.y + offset.y,
        currentMoon.velocity.z + offset.z
    )

private fun retrieveVelocityOffset(positionA: Coordinate, positionB: Coordinate): Coordinate {

    val offsetAX = compareByAxis(positionA.x, positionB.x)
    val offsetAY = compareByAxis(positionA.y, positionB.y)
    val offsetA7 = compareByAxis(positionA.z, positionB.z)

    return Coordinate(offsetAX, offsetAY, offsetA7)
}

private fun compareByAxis(a: Int, b: Int): Int {
    var updateFirstBy = 0

    if (a > b) {
        updateFirstBy = -1
    } else if (a < b) {
        updateFirstBy = 1
    }

    return updateFirstBy
}


typealias Coordinate = Triple<Int, Int, Int>

inline val Coordinate.x: Int get() = first
inline val Coordinate.y: Int get() = second
inline val Coordinate.z: Int get() = third

class State(
    val x1: Int,
    val x2: Int,
    val x3: Int,
    val x4: Int
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (x1 != other.x1) return false
        if (x2 != other.x2) return false
        if (x3 != other.x3) return false
        if (x4 != other.x4) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x1
        result = 31 * result + x2
        result = 31 * result + x3
        result = 31 * result + x4
        return result
    }

    override fun toString(): String {
        return "State(x1=$x1, x2=$x2, x3=$x3, x4=$x4)"
    }
}

class Moon(private val initialPosition: Coordinate, private val initialVelocity : Coordinate) {
    var position = initialPosition
    var velocity = initialVelocity
    var potentialEnergy = 0
    var kineticEnergy = 0
    var totalEnergy = 0
    var states = mutableListOf(initialPosition to initialVelocity)

    fun updatePosition() {
        position = Coordinate(
            this.position.x + this.velocity.x,
            this.position.y + this.velocity.y,
            this.position.z + this.velocity.z
        )
    }

    fun isInitialPosition(): Boolean {
        return position == initialPosition && velocity == initialVelocity
    }

    fun calculateKineticEnergy(): Int {
        kineticEnergy = sumOfAbsoluteValues(velocity.x, velocity.y, velocity.z)
        return kineticEnergy
    }

    fun calculatePotentialEnergy(): Int {
        potentialEnergy = sumOfAbsoluteValues(position.x, position.y, position.z)
        return potentialEnergy
    }

    fun totalEnergy(): Int {
        totalEnergy = calculatePotentialEnergy() * calculateKineticEnergy()
        return totalEnergy
    }

    fun isStateExist(): Boolean = states.contains(this.position to this.velocity)

    fun addNewState() {
        states.plusAssign(this.position to this.velocity)
    }

    private fun sumOfAbsoluteValues(x: Int, y: Int, z: Int): Int {
        return abs(x) + abs(y) + abs(z)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Moon

        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

}

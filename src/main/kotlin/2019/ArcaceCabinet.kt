package `2019`

import main.kotlin.day10.Coordinate
import main.kotlin.day10.x
import main.kotlin.day10.y
import main.kotlin.day13.Tile

private var relativeBase = 0L

fun play(inputs: MutableList<Long>): MutableList<Pair<Coordinate, Int>> =
    play(0L, inputs)

fun play(systemID: Long = 0L, input: MutableList<Long>): MutableList<Pair<Coordinate, Int>> {
    var inputs = input.toMutableList()
    val size = inputs.size
    var i = 0
    var passage = 1

    var xPosition = 0
    var yPosition = 0
    var shape = 0

    var joystick = systemID

    var output = mutableListOf<Pair<Coordinate, Int>>()

    while (i < size) {

        if (checkEndOfProgram(inputs[i])) {
            println("OpCode Write = $passage")
            break
        }

        val firstIndex = inputs[i + 1]
        val firstElement = retrieveElement(
            retrieveFirstElementMode(inputs[i]), inputs, firstIndex
        )

        val opcode = inputs[i] % 10

        val opCodeValue = GameOpCodeEnum.valueOfOpcode(opcode.toInt())

        if(opCodeValue == GameOpCodeEnum.READ){
            var mode = retrieveFirstElementMode(inputs[i])
            var index = firstIndex
            if(mode.toInt() == 2){
                index = firstIndex + relativeBase
            }

            val ballPosition = output.filter { it.second == Tile.BALL.value }.map { it.first.x }
            val paddlePosition = output.filter { it.second == Tile.PADDLE.value }.map { it.first.x }

//            println("Ball = $ballPosition, paddle = $paddlePosition")

            displayGame(output)

            joystick = when {
                paddlePosition.last() > ballPosition.last() -> {
                    -1L
                }
                paddlePosition.last() < ballPosition.last() -> {
                    1L
                }
                else -> {
                    0L
                }
            }

            inputs[index.toInt()] = joystick
            i += RobotOpCodeEnum.READ.offset
            continue
        } else if(opCodeValue == GameOpCodeEnum.WRITE){

            when (passage) {
                1 -> {
                    xPosition = firstElement.toInt()
                    passage++
                }
                2 -> {
                    yPosition = firstElement.toInt()
                    passage++
                }
                3 -> {
                    shape = firstElement.toInt()
                    output.plusAssign(Coordinate(xPosition, yPosition) to shape)
                    passage = 1
                }
            }

            i += RobotOpCodeEnum.WRITE.offset
            continue
        } else if(opCodeValue == GameOpCodeEnum.ADJUST_BASE){
            relativeBase = opCodeValue.operation(firstElement,
                relativeBase
            )
            i += RobotOpCodeEnum.ADJUST_BASE.offset
            continue
        }

        val secondIndex = inputs[i + 2]
        val outputIndex = inputs[i + 3]

        val secondElement = retrieveElement(
            retrieveSecondElementMode(inputs[i]), inputs, secondIndex
        )
        val outputElement = retrieveOutputIndex(
            retrieveOutputElementMode(inputs[i]), outputIndex
        )

         if (opCodeValue == GameOpCodeEnum.JUMP_IF_TRUE || opCodeValue == GameOpCodeEnum.JUMP_IF_FALSE) {
            i = opCodeValue.operation(firstElement, secondElement, i)
        } else {
            inputs[outputElement.toInt()] = opCodeValue!!.operation(firstElement, secondElement)
            i += opCodeValue.offset
        }

    }

//    println(inputs)

    return output
}

private fun displayGame(output: MutableList<Pair<Coordinate, Int>>) {
    val width = output.map { it.first.y }.max() ?: 0
    val height = output.map { it.first.x }.max() ?: 0


    var element = 0

    var drawing = Array(width + 1 ) {Array(height + 1){""} }

    for (coordinate in output){
        var shape = ""
        when (coordinate.second) {
            Tile.EMPTY.value -> shape = " "
            Tile.WALL.value -> shape = "\u2588"
            Tile.BLOCK.value -> shape = "\u2592"
            Tile.PADDLE.value -> shape = "\u2582"
            Tile.BALL.value -> shape = "\u25CD"
        }
        if(coordinate.first.x != -1) {
            drawing[coordinate.first.y][coordinate.first.x] = shape
        }
    }

    for (i in drawing.indices){
        for (j in drawing[i].indices){
            print(drawing[i][j])
        }
        println()
    }

}

private fun checkEndOfProgram(mode: Long): Boolean {
    if (mode.toInt() == GameOpCodeEnum.HALT.number) {
        return true
    }
    return false
}

private fun retrieveElement(
    paramMode: Long,
    inputs: MutableList<Long>,
    index: Long
): Long {

    var element = index
    if (paramMode.toInt() == 0) {
        element = inputs[index.toInt()]
    } else if(paramMode.toInt() == 2){
        element = inputs[relativeBase.toInt() + index.toInt()]
    }
    return element
}

private fun retrieveOutputIndex(
    paramMode: Long,
    index: Long
): Long {
    var element = index

    if(paramMode.toInt() == 2){
        element = relativeBase + index
    }

    return element
}

private fun retrieveFirstElementMode(element: Long) = (element / 100) % 10
private fun retrieveSecondElementMode(element: Long) = (element / 1000) % 10
private fun retrieveOutputElementMode(element: Long) = (element / 10000) % 10

enum class GameOpCodeEnum(val number: Int, val offset: Int = 0) {
    ADD(1, offset = 4) {
        override fun operation(a: Long, b: Long) = addElement.invoke(a, b)
    },

    MULTIPLY(2, offset = 4) {
        override fun operation(a: Long, b: Long) = multiplyElement.invoke(a, b)
    },

    READ(3, offset = 2),

    WRITE(4, offset = 2),

    JUMP_IF_TRUE(5, offset = 3) {
        override fun operation(a: Long, b: Long, index: Int): Int = if (a != 0L) b.toInt() else index.plus(offset)
    },

    JUMP_IF_FALSE(6, offset = 3) {
        override fun operation(a: Long, b: Long, index: Int): Int = if (a == 0L) b.toInt() else index + offset
    },

    LESS_THAN(7, offset = 4) {
        override fun operation(a: Long, b: Long) = if (a < b) 1L else 0L
    },

    EQUALS(8, offset = 4) {
        override fun operation(a: Long, b: Long) = if (a == b) 1L else 0L
    },
    ADJUST_BASE(9, offset = 2) {
        override fun operation(a: Long, b: Long) = addElement.invoke(a, b)
    },

    HALT(99);

    val addElement: (Long, Long) -> Long = Long::plus
    val multiplyElement: (Long, Long) -> Long = Long::times

    open fun operation(a: Long, b: Long): Long = 0
    open fun operation(a: Long, b: Long, index: Int): Int = 0

    companion object {
        private val map = values().associateBy { gameOpCodeEnum: GameOpCodeEnum -> gameOpCodeEnum.number }
        fun valueOfOpcode(type: Int) = map[type]
    }
}

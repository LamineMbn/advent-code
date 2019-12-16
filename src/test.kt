import java.io.FileReader
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.collections.HashMap

object Day11 {
    private const val resourceDirectory = "src/day11/input_AoC_input_1.txt"
    var inputFile = "src/day11/input_AoC_input_1.txt"
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val instructionSet = Scanner(FileReader(inputFile)).nextLine()
        val paintyBoy = LilPaintyRobot(instructionSet, true)
        paintyBoy.paintAway()
        println("Part 1: painty boy painted :" + paintyBoy.paintBoard.keys.size + " squares")
        val paintyBoyPart2 = LilPaintyRobot(instructionSet, false)
        paintyBoyPart2.paintAway()
        println("Part 2 starting to print")
        paintyBoyPart2.printRobotsPaintBoard()
    }

    class LilPaintyRobot internal constructor(instructionSet: String, isPart1: Boolean) {
        private val intcodeComputer: IntcodeComputer
        val paintBoard: HashMap<Cord, PaintColor>
        private var currentLocation = Cord(0, 0)
        private var facing = Direction.UP
        fun paintAway() {
            var done = false
            while (!done) {
                val standingOn = paintBoard.getOrDefault(currentLocation, PaintColor.BLACK)
                val input = if (standingOn == PaintColor.BLACK) 0L else 1L
                intcodeComputer.addInput(input)
                val exitCode = intcodeComputer.runProgram()
                if (exitCode == "EXITED") {
                    done = true
                }
                val colorToPaint = intcodeComputer.outputs.poll()
                val directionTurn = intcodeComputer.outputs.poll()
                if (colorToPaint == 0L) {
                    paintBoard[currentLocation] = PaintColor.BLACK
                } else {
                    paintBoard[currentLocation] = PaintColor.WHITE
                }
                if (directionTurn == 0L) {
                    turnLeft()
                } else {
                    turnRight()
                }
                moveForward()
            }
        }

        private fun turnLeft() {
            facing = when (facing) {
                Direction.UP -> Direction.LEFT
                Direction.DOWN -> Direction.RIGHT
                Direction.LEFT -> Direction.DOWN
                Direction.RIGHT -> Direction.UP
            }
        }

        private fun turnRight() {
            facing = when (facing) {
                Direction.UP -> Direction.RIGHT
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.RIGHT -> Direction.DOWN
            }
        }

        private fun moveForward() {
            currentLocation = when (facing) {
                Direction.UP -> Cord(currentLocation.x, currentLocation.y + 1)
                Direction.DOWN -> Cord(currentLocation.x, currentLocation.y - 1)
                Direction.RIGHT -> Cord(currentLocation.x + 1, currentLocation.y)
                Direction.LEFT -> Cord(currentLocation.x - 1, currentLocation.y)
            }
        }

        fun printRobotsPaintBoard() {
            val minX =
                paintBoard.keys.stream().map { obj: Cord -> obj.x }
                    .min { obj: Int, anotherInteger: Int? -> obj.compareTo(anotherInteger!!) }.get()
            val maxX =
                paintBoard.keys.stream().map { obj: Cord -> obj.x }
                    .max { obj: Int, anotherInteger: Int? -> obj.compareTo(anotherInteger!!) }.get()
            val miny =
                paintBoard.keys.stream().map { obj: Cord -> obj.y }
                    .min { obj: Int, anotherInteger: Int? -> obj.compareTo(anotherInteger!!) }.get()
            val maxy =
                paintBoard.keys.stream().map { obj: Cord -> obj.y }
                    .max { obj: Int, anotherInteger: Int? -> obj.compareTo(anotherInteger!!) }.get()
            for (i in minX..maxX) {
                val stringBuilder = StringBuilder()
                for (j in miny..maxy) {
                    if (PaintColor.WHITE == paintBoard[Cord(i, j)]) {
                        stringBuilder.append("#")
                    } else {
                        stringBuilder.append(" ")
                    }
                }
                println(stringBuilder.toString())
            }
        }

        init {
            intcodeComputer = IntcodeComputer(instructionSet)
            paintBoard = HashMap()
            if (!isPart1) {
                paintBoard[currentLocation] = PaintColor.WHITE
            }
        }
    }

    enum class Direction {
        UP, LEFT, DOWN, RIGHT
    }

    enum class PaintColor {
        BLACK, WHITE
    }

    class Cord internal constructor(val x: Int, val y: Int) {

        override fun equals(obj: Any?): Boolean {
            val other = obj as Cord?
            return this.x == other!!.x && this.y == other.y
        }

        override fun hashCode(): Int {
            return x + y
        }

    }

    class IntcodeComputer internal constructor(instructionSet: String) {
        private val programMem: MutableList<Long>
        private val extraTerrestrialMemory: MutableMap<Long, Long>
        private var programCount = 0
        var relativeBase: Long = 0
        private val inputs: Queue<Long> = LinkedList()
        val outputs: Queue<Long> = LinkedList()

        fun addInput(input: Long) {
            inputs.add(input)
        }

        fun runProgram(): String {
            while (true) {
                val instruction = programMem[programCount++]
                val operationCode = instruction % 100
                var modes = instruction / 100
                val parameterModes = LongArray(3)
                var modesCount = 0
                while (modes > 0) {
                    parameterModes[modesCount++] = modes % 10
                    modes = modes / 10
                }
                if (operationCode == 99L) {
                    return "EXITED"
                } else if (isThreeParameterOpCode(operationCode)) {
                    val firstParameter = getParameterValue(parameterModes[0], programCount++.toLong())
                    val secondParameter = getParameterValue(parameterModes[1], programCount++.toLong())
                    val finalPosition =
                        if (parameterModes[2] == 2L) relativeBase + getParameterValueFromMemory(programCount++.toLong()) else getParameterValueFromMemory(
                            programCount++.toLong()
                        )
                    var valueToSetToFinalPosition: Long
                    valueToSetToFinalPosition = if (operationCode == 1L) {
                        firstParameter + secondParameter
                    } else if (operationCode == 2L) {
                        firstParameter * secondParameter
                    } else if (operationCode == 7L) {
                        if (firstParameter < secondParameter) 1 else 0.toLong()
                    } else if (operationCode == 8L) {
                        if (firstParameter == secondParameter) 1 else 0.toLong()
                    } else {
                        throw RuntimeException("unexpected 3 param opCode...")
                    }
                    setParameterValueToMemory(finalPosition, valueToSetToFinalPosition)
                } else if (operationCode == 3L || operationCode == 4L) {
                    if (operationCode == 3L) {
                        if (inputs.size == 0) {
                            programCount -= 1 //try this again if this gets ran again!
                            return "NEED_INPUT"
                        } else {
                            var parameter1 = programMem[programCount++]
                            if (parameterModes[0] == 2L) {
                                parameter1 = relativeBase + parameter1
                            }
                            setParameterValueToMemory(parameter1, inputs.remove())
                        }
                    } else if (operationCode == 4L) {
                        val output = getParameterValue(parameterModes[0], programCount++.toLong())
                        outputs.add(output)
                    }
                } else if (operationCode == 5L || operationCode == 6L) {
                    val parameter1 = getParameterValue(parameterModes[0], programCount++.toLong())
                    val parameter2 = getParameterValue(parameterModes[1], programCount++.toLong())
                    if (operationCode == 5L) {
                        if (parameter1 != 0L) {
                            programCount = parameter2.toInt()
                        }
                    } else if (operationCode == 6L) {
                        if (parameter1 == 0L) {
                            programCount = parameter2.toInt()
                        }
                    }
                } else if (operationCode == 9L) {
                    relativeBase += getParameterValue(parameterModes[0], programCount++.toLong())
                } else {
                    throw RuntimeException("unexpected Opcode $operationCode")
                }
            }
        }

        fun getParameterValue(parameterMode: Long, paramValue: Long): Long {
            return if (parameterMode == 0L) {
                getParameterValueFromMemory(getParameterValueFromMemory(paramValue))
            } else if (parameterMode == 1L) {
                getParameterValueFromMemory(paramValue)
            } else if (parameterMode == 2L) {
                getParameterValueFromMemory(relativeBase + getParameterValueFromMemory(paramValue))
            } else {
                throw RuntimeException("unexpected parameterMode")
            }
        }

        fun getParameterValueFromMemory(index: Long): Long {
            return if (index >= programMem.size) {
                extraTerrestrialMemory.getOrDefault(index, 0L)
            } else {
                programMem[index.toInt()]
            }
        }

        fun setParameterValueToMemory(index: Long, value: Long) {
            if (index >= programMem.size) {
                extraTerrestrialMemory[index] = value
            } else {
                programMem[index.toInt()] = value
            }
        }

        companion object {
            private fun isThreeParameterOpCode(opCode: Long): Boolean {
                return opCode == 1L || opCode == 2L || opCode == 7L || opCode == 8L
            }
        }

        init {
            programMem = listOf(*instructionSet.split(",".toRegex()).toTypedArray())
                .stream()
                .map { s: String? ->
                    java.lang.Long.valueOf(
                        s
                    )
                }
                .collect(Collectors.toList<Long>())
            extraTerrestrialMemory = HashMap()
        }
    }
}

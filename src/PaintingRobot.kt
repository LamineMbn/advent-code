import java.lang.RuntimeException

private var relativeBase = 0L

fun paint(inputs: MutableList<Long>): Pair<MutableList<Long>, MutableList<Long>> = correctCode(0L to 0L, inputs)

fun paint(systemID: Long = 0L, input: MutableList<Long>): Pair<MutableList<Long>, Int> {
    var inputs = input.toMutableList()
    val size = inputs.size
    var i = 0
    var passage = 0
    var painting = 0

    var output = mutableListOf<Long>()

    while (i < size) {

        if (checkEndOfProgram(inputs[i])) {
            println("OpCode Write = $passage")
            break
        }

        val firstIndex = inputs[i + 1]
        val firstElement = retrieveElement(retrieveFirstElementMode(inputs[i]), inputs, firstIndex)

        val opcode = inputs[i] % 10

        val opCodeValue = RobotOpCodeEnum.valueOfOpcode(opcode.toInt())

        if(opCodeValue == RobotOpCodeEnum.READ){
            var mode = retrieveFirstElementMode(inputs[i])
            var index = firstIndex
            if(mode.toInt() == 2){
                index = firstIndex + relativeBase
            }
            inputs[index.toInt()] = if(output.isEmpty()) systemID else output.last()
            i += RobotOpCodeEnum.READ.offset
            continue
        } else if(opCodeValue == RobotOpCodeEnum.WRITE){
            output.plusAssign(firstElement)
            if(passage % 2 == 0){
                painting++
                passage++
            } else {
                passage++
            }

            i += RobotOpCodeEnum.WRITE.offset
            continue
        } else if(opCodeValue == RobotOpCodeEnum.ADJUST_BASE){
            relativeBase = opCodeValue.operation(firstElement, relativeBase)
            i += RobotOpCodeEnum.ADJUST_BASE.offset
            continue
        }

        val secondIndex = inputs[i + 2]
        val outputIndex = inputs[i + 3]

        val secondElement = retrieveElement(retrieveSecondElementMode(inputs[i]), inputs, secondIndex)
        val outputElement = retrieveOutputIndex(retrieveOutputElementMode(inputs[i]), outputIndex)

         if (opCodeValue == RobotOpCodeEnum.JUMP_IF_TRUE || opCodeValue == RobotOpCodeEnum.JUMP_IF_FALSE) {
            i = opCodeValue.operation(firstElement, secondElement, i)
        } else {
            inputs[outputElement.toInt()] = opCodeValue!!.operation(firstElement, secondElement)
            i += opCodeValue.offset
        }
    }

//    println(inputs)

    return output to painting;
}

private fun checkEndOfProgram(mode: Long): Boolean {
    if (mode.toInt() == RobotOpCodeEnum.HALT.number) {
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

enum class RobotOpCodeEnum(val number: Int, val offset: Int = 0) {
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
        private val map = values().associateBy { robotOpCodeEnum: RobotOpCodeEnum -> robotOpCodeEnum.number }
        fun valueOfOpcode(type: Int) = map[type]
    }
}

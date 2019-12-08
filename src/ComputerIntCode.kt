fun correctCode(systemID: Int = 0, inputs:   MutableList<Int>) : MutableList<Int> {
    val size = inputs.size
    var i = 0

    var output: List<Int> = ArrayList();

    while (i < size) {

        var opcode = inputs[i]
        val firstIndex: Int = inputs[i + 1]

        if (opcode == OpCodeEnum.HALT.number) {
            break
        } else {
            opcode = inputs[i] % 10
        }

        if (opcode == 3) {
            inputs[firstIndex] = systemID;
            i += 2
            continue
        }

        if (opcode == 4) {
            output += inputs[firstIndex]
            println(inputs[firstIndex])
            println(output)
            i += 2
            continue
        }

        val secondIndex: Int = inputs[i + 2]
        val outputIndex: Int = inputs[i + 3]

        val firstIsPositionMode = (inputs[i] / 100) % 10 == 0
        val secondIsPositionMode = (inputs[i] / 1000) % 10 == 0

        val firstElement = if (firstIsPositionMode) inputs[firstIndex] else firstIndex
        val secondElement = if (secondIsPositionMode) inputs[secondIndex] else secondIndex


        val addElement: (Int, Int) -> Int = Int::plus
        val multiplyElement: (Int, Int) -> Int = Int::times


        if (opcode == OpCodeEnum.ADDS.number) {
            inputs[outputIndex] = addElement.invoke(firstElement, secondElement)
            i += 4
            continue
        } else if (opcode == OpCodeEnum.MULTIPLY.number) {
            inputs[outputIndex] = multiplyElement(firstElement, secondElement)
            i += 4
            continue
        } else if (opcode == 5) {
            if (firstElement != 0) {
                i = secondElement
            } else {
                i += 3
            }
            continue
        } else if (opcode == 6) {
            if (firstElement == 0) {
                i = secondElement
            } else {
                i += 3
            }
            continue
        } else if (opcode == 7) {
            inputs[outputIndex] = if (firstElement < secondElement) 1 else 0
            i += 4
            continue
        } else if (opcode == 8) {
            inputs[outputIndex] = if (firstElement == secondElement) 1 else 0
            i += 4
            continue
        }
    }

    return inputs;
}

enum class OpCodeEnum(val number: Int) {
    ADDS(1), MULTIPLY(2), HALT(99)
}

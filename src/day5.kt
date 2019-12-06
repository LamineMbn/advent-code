
fun main(){
//    var computerInputs : List<Int> = listOf(3,4,1002,4,3,4,33)
    var computerInputs : List<Int> = listOf(3,225,1,225,6,6,1100,1,238,225,104,0,1102,40,93,224,1001,224,-3720,224,4,224,102,8,223,223,101,3,224,224,1,224,223,223,1101,56,23,225,1102,64,78,225,1102,14,11,225,1101,84,27,225,1101,7,82,224,1001,224,-89,224,4,224,1002,223,8,223,1001,224,1,224,1,224,223,223,1,35,47,224,1001,224,-140,224,4,224,1002,223,8,223,101,5,224,224,1,224,223,223,1101,75,90,225,101,9,122,224,101,-72,224,224,4,224,1002,223,8,223,101,6,224,224,1,224,223,223,1102,36,63,225,1002,192,29,224,1001,224,-1218,224,4,224,1002,223,8,223,1001,224,7,224,1,223,224,223,102,31,218,224,101,-2046,224,224,4,224,102,8,223,223,101,4,224,224,1,224,223,223,1001,43,38,224,101,-52,224,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1102,33,42,225,2,95,40,224,101,-5850,224,224,4,224,1002,223,8,223,1001,224,7,224,1,224,223,223,1102,37,66,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1007,226,677,224,1002,223,2,223,1005,224,329,1001,223,1,223,1007,226,226,224,1002,223,2,223,1006,224,344,101,1,223,223,1107,677,226,224,102,2,223,223,1006,224,359,1001,223,1,223,108,677,677,224,1002,223,2,223,1006,224,374,1001,223,1,223,107,677,677,224,1002,223,2,223,1005,224,389,101,1,223,223,8,677,677,224,1002,223,2,223,1005,224,404,1001,223,1,223,108,226,226,224,1002,223,2,223,1005,224,419,101,1,223,223,1008,677,677,224,1002,223,2,223,1005,224,434,101,1,223,223,1008,226,226,224,1002,223,2,223,1005,224,449,101,1,223,223,7,677,226,224,1002,223,2,223,1006,224,464,1001,223,1,223,7,226,226,224,1002,223,2,223,1005,224,479,1001,223,1,223,1007,677,677,224,102,2,223,223,1005,224,494,101,1,223,223,1108,677,226,224,102,2,223,223,1006,224,509,1001,223,1,223,8,677,226,224,102,2,223,223,1005,224,524,1001,223,1,223,1107,226,226,224,102,2,223,223,1006,224,539,1001,223,1,223,1008,226,677,224,1002,223,2,223,1006,224,554,1001,223,1,223,1107,226,677,224,1002,223,2,223,1006,224,569,1001,223,1,223,1108,677,677,224,102,2,223,223,1005,224,584,101,1,223,223,7,226,677,224,102,2,223,223,1006,224,599,1001,223,1,223,1108,226,677,224,102,2,223,223,1006,224,614,101,1,223,223,107,226,677,224,1002,223,2,223,1005,224,629,101,1,223,223,108,226,677,224,1002,223,2,223,1005,224,644,101,1,223,223,8,226,677,224,1002,223,2,223,1005,224,659,1001,223,1,223,107,226,226,224,1002,223,2,223,1006,224,674,101,1,223,223,4,223,99,226)

    val output = correctCode(5, computerInputs.toMutableList())

    println(output)

}

private fun correctCode(systemID: Int, inputs:   MutableList<Int>) : MutableList<Int> {
    val size = inputs.size
    var i = 0

    var airConditioning : List<Int> = ArrayList();

    while (i < size) {

        var opcode = inputs[i]
        val firstIndex: Int = inputs[i + 1]

        if (opcode == OpCodeEnum.HALT.number) {
            break
        } else {
            opcode = inputs[i]  % 10
        }

        if(opcode == 3){
            inputs[firstIndex] = systemID;
            i += 2
            continue
        }

        if(opcode == 4){
            airConditioning += inputs[firstIndex]
            println(inputs[firstIndex])
            i += 2
            continue
        }

        val secondIndex: Int = inputs[i + 2]
        val outputIndex: Int = inputs[i + 3]

        val firstIsPositionMode = (inputs[i] / 100)  % 10 == 0
        val secondIsPositionMode = (inputs[i] / 1000)  % 10 == 0

        val firstElement = if(firstIsPositionMode) inputs[firstIndex] else firstIndex
        val secondElement = if(secondIsPositionMode) inputs[secondIndex] else secondIndex


        val addElement : (Int, Int) -> Int = Int::plus
        val multiplyElement : (Int, Int) -> Int = Int::times


        if (opcode == OpCodeEnum.ADDS.number) {
            inputs[outputIndex] = addElement.invoke(firstElement,secondElement)
            i += 4
            continue
        } else if (opcode == OpCodeEnum.MULTIPLY.number) {
            inputs[outputIndex] = multiplyElement(firstElement,secondElement)
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
            inputs[outputIndex] = if(firstElement < secondElement) 1 else 0
            i += 4
            continue
        } else if (opcode == 8) {
            inputs[outputIndex] = if(firstElement == secondElement) 1 else 0
            i += 4
            continue
        }

    }

    println(airConditioning)

    return inputs;
}

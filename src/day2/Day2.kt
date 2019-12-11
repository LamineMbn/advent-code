package day2

import correctCode
import readFileLineByLineUsingForEachLine

fun main(){

    val computerInputs : List<Long> = readFileLineByLineUsingForEachLine("src/day2/input_Aoc_1.txt").flatMap{ it.split(",") }.map { it.toLong() }

    for (i in 0..99){
        for (j in 0..99){
            val inputs : MutableList<Long> = computerInputs.toMutableList()
            inputs[1] = i.toLong(); inputs[2] = j.toLong()
            val output = correctCode(0L to 0L, inputs)

            if(output.second[0] == 19690720L){
                val out = 100 * i + j
                check(out == 4112)
                println("noun =$out")
            }
        }
    }
}

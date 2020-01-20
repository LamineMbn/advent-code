package year2019.day14

import readFile
import java.util.function.Function

private val equations: List<String> =
    readFile("src/main/kotlin/2019/day14/input_AoC_input_0.txt")

private val memberTransformer: MemberTransformer =
    MemberTransformer()
private val equationTransformer: EquationTransformer =
    EquationTransformer()

typealias Equation = MutableMap<String, Pair<Int, List<Element>>>


typealias Element = Pair<Int, String>

inline val Element.quantity get() = first
inline val Element.element get() = second

fun main() {

    val input : Equation = mutableMapOf()

    equations.map { it.split(" => ") }.forEach {
        run {
            input.plusAssign((equationTransformer.apply(it)))
        }
    }

//    input.keys.map { input.orbitChains(it) }
    
//    println(input)

    produce(1, input)

//    val indirectOrbit = input.keys.map { input.orbitChains(it).size }.sum()
}

private fun produce(quantity : Long, input : Equation){
    var materials = mutableListOf(quantity to ElementEnum.FUEL.toString())

    while (true) {
        val (n, key) = materials.find { (n, key) -> key != ElementEnum.ORE.toString() && n > 0} ?: break
        val (quantity, materials) = checkNotNull(input[key]) {"null key = $key"}
        println("Quantity = $quantity material = $materials")
    }
}

//tailrec fun Equation.orbitChains(element: Element, chain: List<Element> = emptyList()): List<Element> {
//
//    return if (keys.stream().anyMatch { it.element == element.element }) {
//        val key: Element = keys.first { it.element == element.element }
//        var consumers = getValue(key)
//        println(consumers)
//        orbitChains(key, consumers)
//    } else
//        chain
//}

enum class ElementEnum {
    ORE, FUEL
}

class EquationTransformer : Function<List<String>, MutableMap<String, Pair<Int, List<Element>>>> {

    override fun apply(t: List<String>): MutableMap<String, Pair<Int, List<Element>>> {

        val rightSide = memberTransformer.apply(t.last())
        println(rightSide)

        val leftSide = t.first().split(", ").map { memberTransformer.apply(it) }
        println(leftSide)

        return mutableMapOf(rightSide.second to (rightSide.first to leftSide))
    }
}


class MemberTransformer : Function<String, Element> {

    override fun apply(t: String): Element {
        val splitedString = t.split(" ")
        return Pair(splitedString[0].toInt(), splitedString[1])
    }
}

package year2019.day22

import year2019.day22.DealTechnique.*
import readFile
import kotlin.math.abs

private val factoryOrder: List<String> =
    readFile("src/main/kotlin/day22/input_AoC_input_1.txt")

fun main() {

    var numberOfCards = 10007
    val factoryOrderMap = mutableListOf<Pair<DealTechnique, Int>>()

    factoryOrder.forEach {
        run {
            var firstDigit = it.indexOfFirst { it.isDigit() }
            var deal = it.substring(0)
            var cards = 0
            if (firstDigit != -1) {
                deal = it.substring(0, firstDigit - 1)
                cards = if(it[firstDigit -1] == '-') {
                    it.substring(firstDigit-1).toInt()
                } else{
                    it.substring(firstDigit).toInt()
                }

            }
            factoryOrderMap.plusAssign(DealTechnique.getEnumFromTechnique(deal) to cards)
        }
    }

    var cards = IntRange(0, numberOfCards-1).toMutableList()

//    val test = mutableListOf(INTO_NEW_STACK to 0, CUT to -2 , DEAL_WITH_INCREMENT to 7, CUT to 8 , CUT to -4 , DEAL_WITH_INCREMENT to 7, CUT to 3, DEAL_WITH_INCREMENT to 9, DEAL_WITH_INCREMENT to 3, CUT to -1)
    val test = mutableListOf(DEAL_WITH_INCREMENT to 7)
//    val test = mutableListOf(DEAL_WITH_INCREMENT to 7, DEAL_WITH_INCREMENT to 9, CUT to -2)

    factoryOrderMap.forEach { dealTechnique ->
        run {
            cards = dealTechnique.first.deal(cards, dealTechnique.second).toMutableList()
        }
    }

    println(cards)
    val cardIndex = cards.indexOf(2019)
    check(cardIndex == 6850)
    println("2019 cards = $cardIndex")

    numberOfCards = 10
    cards = IntRange(0, numberOfCards-1).toMutableList()

    test.forEach { dealTechnique ->
        run {
            cards = dealTechnique.first.deal(cards, dealTechnique.second).toMutableList()
        }
    }
    println(cards)
    test.forEach { dealTechnique ->
        run {
            cards = dealTechnique.first.deal(cards, dealTechnique.second).toMutableList()
        }
    }

    println(cards)


    println(test)
}


enum class DealTechnique(val technique: String = "") {
    INTO_NEW_STACK("deal into new stack") {
        override fun deal(cards: List<Int>, n: Int) =  cards.reversed()
    },
    CUT("cut") {
        override fun deal(pCards: List<Int>, n: Int): List<Int> {
            var cards = mutableListOf<Int>()
            var cardsToTake: MutableList<Int>
            if (n > 0) {
                cardsToTake = pCards.take(n).toMutableList()
                cards = pCards.subList(n, pCards.size).toMutableList()
                cards.plusAssign(cardsToTake)
            } else {
                cardsToTake = pCards.takeLast(abs(n)).toMutableList()
                cards.plusAssign(cardsToTake)
                cards.plusAssign(pCards.subList(0, pCards.size - abs(n)).toMutableList())
            }


            return cards
        }
    },
    DEAL_WITH_INCREMENT("deal with increment") {
        override fun deal(pCards: List<Int>, n: Int): List<Int> {
            var cards = IntArray(pCards.size)

            var i = 0
            var j = 0

            while (i < pCards.size) {
                cards[j % pCards.size] = pCards[i]
                i++
                j += n
            }

            return cards.toList()
        }
    };

    abstract fun deal(cards: List<Int>, n: Int = 0): List<Int>;

    companion object {
        fun getEnumFromTechnique(technique: String): DealTechnique {
            return values().first { it.technique == technique.trim() }
        }
    }

}

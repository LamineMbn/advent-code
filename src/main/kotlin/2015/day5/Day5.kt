package main.kotlin.day5

import readFileLineByLine

private val strings: List<String> =
    readFileLineByLine("src/main/kotlin/2015/day5/input_Aoc_1.txt")

fun main() {

    val numberOfNiceString = strings.filter { containsVowels(it) && containsDoubles(it) && !containsForbiddenWords(it) }.count()
    
    println(numberOfNiceString)

}

private fun containsVowels(s: String): Boolean {
    val letters = s.toCharArray()
    val numberOfVowels = letters.filter { isVowel(it) }.count()

    return numberOfVowels >= 3
}

private fun containsDoubles(s: String): Boolean {
    val letters = s.toCharArray()
    var numberOfDouble = 0
    for (i in 0 until letters.size - 1) {
        if (isDouble(letters[i], letters[i + 1])) {
            numberOfDouble++
        }
    }

    return numberOfDouble >= 1
}

private fun containsForbiddenWords(s: String): Boolean {
    val letters = s.toCharArray()
    val lettersToCompare = retrieveLettersComb(letters)

    return lettersToCompare.any { isForbidden(it) }
}

private fun retrieveLettersComb(letters: CharArray): MutableList<String> {
    val lettersToCompare = mutableListOf<String>()

    for (i in 0 until letters.size - 1) {
        lettersToCompare.plusAssign(letters[i].toString() + letters[i + 1].toString())
    }
    return lettersToCompare
}

private fun isVowel(c: Char): Boolean {
    if (c in listOf('a', 'e', 'i', 'o', 'u')) {
        return true
    }
    return false
}

private fun isDouble(first: Char, second: Char): Boolean {
    return first == second
}

private fun isForbidden(s: String): Boolean {
    if (s in listOf("ab", "cd", "pq", "xy")) {
        return true
    }
    return false
}

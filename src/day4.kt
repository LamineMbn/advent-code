fun main() {

    var numberOfCorrectPassorwd = 0;

    for (i in 264360..746325) {
        if (passwordIsCorrect(i.toString())) {
            numberOfCorrectPassorwd++
        }
    }

    println(numberOfCorrectPassorwd)

}

private fun passwordIsCorrect(password: String): Boolean {

    val hasRightSize = password.length == 6

    val digitsAreIncreasing = digitsAreIncreasing(password)

    var twoAdjacentDigitsAreTheSame = false

    if (digitsAreIncreasing) {
        twoAdjacentDigitsAreTheSame = twoAdjacentDigitsAreTheSame(password)
    }


    return hasRightSize && digitsAreIncreasing && twoAdjacentDigitsAreTheSame
}

private fun twoAdjacentDigitsAreTheSame(digits: CharSequence): Boolean {
    var twoAdjacentDigitsAreTheSame = false

//    val size = digits.length

    val count = digits.groupingBy { it }.eachCount()

//    println(count)

    for(value in count.values){
        if(value == 2 ){
            twoAdjacentDigitsAreTheSame = true
        }
    }

    return twoAdjacentDigitsAreTheSame
}

private fun digitsAreIncreasing(digits: CharSequence): Boolean {
    var digitsAreIncreasing = true;
    val size = digits.length

    for (i in 0 until size - 1) {
        if (digits[i] > digits[i + 1]) {
            digitsAreIncreasing = false;
        }
    }

    return digitsAreIncreasing
}

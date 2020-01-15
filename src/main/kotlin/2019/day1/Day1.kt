package main.kotlin.day1

import kotlin.math.floor

var masses : List<Int> = listOf(132791,78272,114679,60602,59038,69747,61672,147972,92618,70186,125826,61803,78112,124864,58441,113062,105389,125983,90716,75544,148451,73739,127762,146660,128747,148129,138635,80095,60241,145455,98730,59139,146828,113550,91682,107415,129207,147635,104583,102245,73446,148657,96364,52033,69964,63609,98207,73401,65511,115034,126179,96664,85394,128472,79017,93222,55267,102446,133150,148985,95325,57713,77370,60879,111977,99362,91581,55201,137670,127159,128324,77217,86378,112847,108265,80355,75650,106222,67793,113891,74508,139463,69972,122753,135854,127770,101085,98304,61451,146719,61225,60468,83613,137436,126303,78759,70081,110671,113234,111563)

//fun day2.day7.day3.day4.day5.day6.`2019`.main9`.main.kotlin.`2019`.main(){
//    val fuelNeeded : Int = day1.getMasses.stream().mapToDouble(Int::toDouble).map { mass -> `2019`.main.kotlin.calculateFuelOfFuel(mass) }.sum().toInt()
//
//    print(fuelNeeded)
//}

fun calculateFuelOfFuel(pMass : Double) : Double {
    var fuel = pMass
    var sum = 0.0

    while (calculateFuel(fuel) >= 0){
        fuel = calculateFuel(fuel)
        sum += fuel
    }

    return sum
}

fun calculateFuel(mass : Double) = divideByThreeAndFloor(mass) - 2

fun divideByThreeAndFloor (mass: Double) = floor(mass / 3);

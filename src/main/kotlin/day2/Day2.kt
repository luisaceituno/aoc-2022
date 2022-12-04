package day2
import java.io.File

fun main() {
    val input = File("src/main/kotlin/day2/input.txt").readLines()
    round1(input)
    round2(input)
}

fun round1(lines: List<String>) {
    var score = 0
    for (line in lines) {
        val opponents = decode(line[0])
        val mine = decode(line[2])
        score += getPoints(mine, opponents) + getHandScore(mine)
    }
    println("Round 1: $score")
}

fun round2(lines: List<String>) {
    var score = 0
    for (line in lines) {
        val opponents = decode(line[0])
        val neededResult = line[2]
        val mine = complement(opponents, neededResult)
        score += getPoints(mine, opponents) + getHandScore(mine)
    }
    println("Round 2: $score")
}

enum class Hand {
    ROCK, PAPER, SCISSORS
}

fun decode(hand: Char): Hand {
    return when(hand) {
        'X', 'A' -> Hand.ROCK
        'Y', 'B' -> Hand.PAPER
        'Z', 'C' -> Hand.SCISSORS
        else -> throw IllegalArgumentException()
    }
}

fun complement(opponentsHand: Hand, neededResult: Char): Hand {
    return when(neededResult) {
        'X' -> when(opponentsHand) {
            Hand.ROCK -> Hand.SCISSORS
            Hand.PAPER -> Hand.ROCK
            Hand.SCISSORS -> Hand.PAPER
        }
        'Y' -> opponentsHand
        'Z' -> when(opponentsHand) {
            Hand.ROCK -> Hand.PAPER
            Hand.PAPER -> Hand.SCISSORS
            Hand.SCISSORS -> Hand.ROCK
        }
        else -> throw IllegalArgumentException()
    }
}

fun getPoints(myHand: Hand, opponentsHand: Hand): Int {
    if (myHand == opponentsHand) {
        return 3
    }
    return when(myHand) {
        Hand.ROCK -> if(opponentsHand == Hand.SCISSORS) { 6 } else { 0 }
        Hand.PAPER -> if(opponentsHand == Hand.ROCK) { 6 } else { 0 }
        Hand.SCISSORS -> if(opponentsHand == Hand.PAPER) { 6 } else { 0 }
    }
}

fun getHandScore(hand: Hand): Int {
    return when(hand) {
        Hand.ROCK -> 1
        Hand.PAPER -> 2
        Hand.SCISSORS -> 3
    }
}
package day6

import util.readInputForDay

fun main() {
    val input = readInputForDay(6).first()
    println("part 1: ${firstIndexProceedingNDistinctChars(input, 4)}")
    println("part 2: ${firstIndexProceedingNDistinctChars(input, 14)}")
}

fun firstIndexProceedingNDistinctChars(input: String, n: Int): Int {
    return n + input
            .windowedSequence(n, 1)
            .indexOfFirst { it.toSet().size == n }
}
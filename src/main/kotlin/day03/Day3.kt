package day03

import java.io.File

val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

fun main() {
    val lines = File("src/main/kotlin/day3/input.txt").readLines().filter { it.isNotEmpty() }
    part1(lines)
    part2(lines)
}

fun part1(lines: List<String>) {
    val halves = lines.map { it.chunked(it.length / 2).map { s -> s.toCharArray() } }
    val score = halves.map { getCommonChar(it) }.sumOf { getPriority(it) }
    println("part 1: $score")
}

fun part2(lines: List<String>) {
    val rucksacks = lines.map { it.toCharArray() }
    val groups = rucksacks.chunked(3)
    val score = groups.map { getCommonChar(it) }.sumOf { getPriority(it) }
    println("part 2: $score")
}

fun getCommonChar(groups: Collection<CharArray>): Char {
    val rest = groups.drop(1)
    return groups.first().asSequence().filter { c -> rest.all { it.contains(c) } }.first()
}

fun getPriority(item: Char): Int {
    return 1 + chars.indexOf(item)
}
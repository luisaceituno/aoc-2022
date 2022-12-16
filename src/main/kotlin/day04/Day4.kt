package day4

import util.readInputForDay
import kotlin.math.min

fun main() {
    val lines = readInputForDay(4)
    part1(lines)
    part2(lines)
}

fun part1(lines: List<String>) {
    val pairs = lines.map { it.split(",").map { r -> asRange(r) } }
    val overlaps = pairs.filter { (r1, r2) -> r1.intersect(r2).size == min(r1.length(), r2.length()) }.size
    println("part1: $overlaps")
}

fun part2(lines: List<String>) {
    val pairs = lines.map { it.split(",").map { r -> asRange(r) } }
    val overlaps = pairs.filter { (r1, r2) -> r1.intersect(r2).isNotEmpty() }.size
    println("part2: $overlaps")
}

fun asRange(section: String): IntRange {
    val (start, end) = section.split("-").map { it.toInt() }
    return IntRange(start, end)
}

fun IntRange.length(): Int {
    return this.last + 1 - this.first
}

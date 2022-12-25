package day20

import util.readInputForDay

fun main() {
    val lines = readInputForDay(20).filter { it.isNotBlank() }
    println("Part1: ${decode(lines)}")
    println("Part2: ${decode(lines, 10, 811589153)}")
}

fun decode(lines: List<String>, rounds: Int = 1, seed: Long = 1): Long {
    val values = lines.map { it.toLong() * seed }
    val working = values.indices.toMutableList()
    val size = working.size
    for (round in 1..rounds) {
        for (i in values.indices) {
            val movement = values[i]
            val initialPos = working.indexOf(i)
            working.removeAt(initialPos)
            val wrapped = (initialPos + movement) % (size - 1)
            val next = if (wrapped > 0) wrapped else size - 1 + wrapped
            working.add(next.toInt(), i)
        }
    }
    val result = working.map { values[it] }
    val startAt = working.indexOf(values.indexOf(0))
    val c1000 = result[(startAt + 1000) % size]
    val c2000 = result[(startAt + 2000) % size]
    val c3000 = result[(startAt + 3000) % size]
    return c1000 + c2000 + c3000
}

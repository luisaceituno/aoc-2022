package day9

import util.readInputForDay
import kotlin.math.abs

fun main() {
    val lines = readInputForDay(9)
    val moves = lines.map { it.split(" ") }.map { (dir, len) -> Move(dir.first(), len.toInt()) }
    part1(moves)
    part2(moves)
}

fun part1(moves: List<Move>) {
    val field = Field()
    val tail = Knot(0, 0, null, field)
    val head = Knot(0, 0, tail)
    for (move in moves) {
        head.move(move)
    }
    println("Part1: ${field.countVisitedAtLeast(1)}")
}

fun part2(moves: List<Move>) {
    val field = Field()
    val tail = Knot(0, 0, null, field)
    val k8 = Knot(0, 0, tail)
    val k7 = Knot(0, 0, k8)
    val k6 = Knot(0, 0, k7)
    val k5 = Knot(0, 0, k6)
    val k4 = Knot(0, 0, k5)
    val k3 = Knot(0, 0, k4)
    val k2 = Knot(0, 0, k3)
    val k1 = Knot(0, 0, k2)
    val head = Knot(0, 0, k1)
    for (move in moves) {
        head.move(move)
    }
    println("Part2: ${field.countVisitedAtLeast(1)}")
}

data class Move(val dir: Char, val len: Int)
data class Knot(var x: Int, var y: Int, val follower: Knot? = null, val field: Field? = null) {
    init {
        field?.mark(x, y)
    }

    fun move(m: Move) {
        when (m.dir) {
            'R' -> follow(Knot(x + m.len + 1, y))
            'L' -> follow(Knot(x - m.len - 1, y))
            'U' -> follow(Knot(x, y - m.len - 1))
            'D' -> follow(Knot(x, y + m.len + 1))
        }
    }

    private fun follow(k: Knot) {
        while (abs(k.x - x) > 1 || abs(k.y - y) > 1) {
            step(k)
            field?.mark(x, y)
            follower?.follow(this)
        }
    }

    private fun step(k: Knot) {
        x += if (k.x > x) 1 else if (k.x < x) -1 else 0
        y += if (k.y > y) 1 else if (k.y < y) -1 else 0
    }
}

class Field {
    // y -> x -> passes
    private val field = mutableMapOf<Int, MutableMap<Int, Int>>()

    fun mark(x: Int, y: Int) {
        val row = field.getOrPut(y) { mutableMapOf() }
        row.compute(x) { _, prev -> (prev ?: 0) + 1 }
    }

    fun countVisitedAtLeast(times: Int): Int {
        return field.values.sumOf { it.values.count { visits -> visits >= times } }
    }
}
package day14

import util.readInputForDay

fun main() {
    val lines = readInputForDay(14)
    val rockPositions = parseSpecs(lines)
    part1(rockPositions)
    part2(rockPositions)
}

fun part1(rockPositions: Set<Pos>) {
    val terrain = Terrain(rockPositions)
    while (terrain.addSandUnit(Pos(0, 500))) {
    }
    println("Part1: ${terrain.sandUnits.size}")
}

fun part2(rockPositions: Set<Pos>) {
    val terrain = Terrain(rockPositions)
    val floorY = terrain.highestRockY + 2
    while (terrain.addSandUnit(Pos(0, 500), floorY)) {
    }
    println("Part2: ${terrain.sandUnits.size}")
}

class Terrain(private val rockPositions: Set<Pos>) {
    val highestRockY = rockPositions.maxOf { it.y }
    val sandUnits = mutableSetOf<Pos>()

    fun addSandUnit(start: Pos, floorY: Int = Int.MAX_VALUE): Boolean {
        if (sandUnits.contains(start)) return false
        var current = getNextSandPos(start, floorY)
        while (true) {
            val next = getNextSandPos(current, floorY)
            if (next == current) {
                sandUnits.add(next)
                return true
            }
            if (floorY == Int.MAX_VALUE && next.y >= highestRockY) return false
            current = next
        }
    }

    private fun getNextSandPos(sandPos: Pos, floorY: Int): Pos {
        val (y, x) = sandPos
        for (next in arrayOf(Pos(y + 1, x), Pos(y + 1, x - 1), Pos(y + 1, x + 1))) {
            if (!rockPositions.contains(next) && !sandUnits.contains(next) && next.y < floorY) {
                return next
            }
        }
        return sandPos
    }
}

fun parseSpecs(specs: List<String>): Set<Pos> {
    val containedPositions = mutableSetOf<Pos>()
    for (spec in specs) {
        val corners = spec.split(" -> ")
            .map { it.split(",").map { n -> n.toInt() } }
            .map { (x, y) -> Pos(y, x) }
        val pairs = corners.windowed(2)
        for (pair in pairs) {
            containedPositions.addAll(pair[0].rangeTo(pair[1]))
        }
    }
    return containedPositions
}

typealias Pos = Pair<Int, Int>

val Pos.y: Int
    get() = this.first
val Pos.x: Int
    get() = this.second

fun Pos.rangeTo(other: Pos): List<Pos> {
    val (y1, y2) = listOf(this.y, other.y).sorted()
    val (x1, x2) = listOf(this.x, other.x).sorted()
    return (y1..y2).flatMap { y -> (x1..x2).map { x -> Pos(y, x) } }
}

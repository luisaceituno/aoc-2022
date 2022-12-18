package day18

import util.XYZ
import util.readInputForDay
import java.util.*

fun main() {
    val cubes = readInputForDay(18)
        .map { it.split(",").map(String::toLong) }
        .map { (x, y, z) -> XYZ(x, y, z) }
        .toSet()
    var freeSides = 0
    val borders = HashSet<XYZ>()
    for (cube in cubes) {
        val freeCubeAdjacent = cube.getFaceAdjacent().filter { it !in cubes }
        freeSides += freeCubeAdjacent.size
        borders.addAll(freeCubeAdjacent)
    }
    println("Part1: $freeSides")

    val membrane = HashSet<XYZ>(borders.size)
    val frontier = LinkedList<XYZ>()
    val membraneStart = borders.minBy { it.x }
    membrane.add(membraneStart)
    frontier.add(membraneStart)
    while (frontier.isNotEmpty()) {
        val current = frontier.removeFirst()
        val helpers = HashSet<XYZ>()
        for (adjacent in current.getFaceAdjacent().filter { it !in cubes }) {
            if (adjacent in borders) {
                val isFrontier = membrane.add(adjacent)
                if (isFrontier) frontier.add(adjacent)
            } else {
                helpers.add(adjacent)
            }
        }
        for (helper in helpers) {
            for (helperAdjacent in helper.getFaceAdjacent().filter { it in borders }) {
                val isFrontier = membrane.add(helperAdjacent)
                if (isFrontier) frontier.add(helperAdjacent)
            }
        }
    }
    val outsideFaceCount = membrane.sumOf { m -> m.getFaceAdjacent().count { it in cubes } }
    println("Part2: $outsideFaceCount")
}
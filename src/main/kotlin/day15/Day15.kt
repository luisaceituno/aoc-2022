package day15

import util.YX
import util.readInputForDay
import kotlin.math.abs

fun main() {
    val probes = readInputForDay(15).map { parseProbe(it) }
    part1(probes)
    part2(probes)
}

private fun part1(probes: List<Probe>) {
    val minX = probes.minOf { it.pos.x - it.radius }
    val maxX = probes.maxOf { it.pos.x + it.radius }
    var covered = 0
    for (x in minX..maxX) {
        val point = YX(2000000, x)
        if (probes.any { it.isImpossibleLocationForBeacon(point) }) covered++
    }
    println("Part1: $covered")
}

fun part2(probes: List<Probe>) {
    val searchField = 4000000L
    for (y in 0L..searchField) {
        var x = 0L
        while (x <= searchField) {
            val point = YX(y, x)
            val containingProbe = probes.firstOrNull { it.isInRadius(point) }
            if (containingProbe == null) {
                println("Part2: ${point.x * 4000000 + point.y}")
                return
            } else {
                x = containingProbe.getFirstXOutsideRadius(y)
            }
        }
    }
}

fun parseProbe(line: String): Probe {
    val (probeSpec, beaconSpec) = line.split(":").map { it.substring(it.indexOf('x')) }
    return Probe(parseYX(probeSpec), parseYX(beaconSpec))
}

fun parseYX(spec: String): YX {
    val (x, y) = spec.split(",").map { xy -> xy.split("=")[1].toLong() }
    return YX(y, x)
}

class Probe(val pos: YX, private val beacon: YX) {
    val radius = pos.manhattanDistanceTo(beacon)

    override fun toString(): String {
        return "Probe(pos=$pos, beacon=$beacon)"
    }

    fun isInRadius(point: YX): Boolean {
        return pos.manhattanDistanceTo(point) <= radius
    }

    fun isImpossibleLocationForBeacon(point: YX): Boolean {
        return point != beacon && isInRadius(point)
    }

    fun getFirstXOutsideRadius(row: Long): Long {
        return pos.x + radius - abs(row - pos.y) + 1
    }
}

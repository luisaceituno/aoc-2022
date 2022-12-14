package day12

import util.readInputForDay
import java.util.*

fun main() {
    println("Part1: ${getShortestDistanceToEndFromPosition('S')}")
    println("Part2: ${getShortestDistanceToEndFromPosition('a')}")
}

fun getShortestDistanceToEndFromPosition(start: Char): Int {
    val inputMap = readInputForDay(12).map { it.toCharArray() }
    val height = inputMap.size
    val width = inputMap[0].size
    val elevations = Array(height) { y -> inputMap[y].map { if (it == 'S') 'a' else if (it == 'E') 'z' else it } }
    val distances = Array(height) { IntArray(width) { Int.MAX_VALUE } }
    val starts = inputMap
        .flatMapIndexed { y, row -> row.mapIndexed { x, c -> if (c == start) Pos(y, x) else Pos(-1, -1) } }
        .filter { validPos(it) }
    val e = inputMap.mapIndexed { y, row -> Pos(y, row.indexOf('E')) }.first { (_, x) -> x >= 0 }

    val reachablePositions = LinkedList<Pos>()
    for (s in starts) {
        distances[s.first][s.second] = 0
        reachablePositions.add(s)
    }
    while (distances[e.first][e.second] == Int.MAX_VALUE) {
        val (y, x) = reachablePositions.removeFirst()
        val distance = distances[y][x] + 1
        for ((nextY, nextX) in crossMoves(y, x, height, width)) {
            if (elevations[nextY][nextX] <= elevations[y][x] + 1
                && distances[nextY][nextX] > distance
            ) {
                distances[nextY][nextX] = distance
                reachablePositions.add(Pos(nextY, nextX))
            }
        }
    }
    return distances[e.first][e.second]
}

typealias Pos = Pair<Int, Int>

fun crossMoves(y: Int, x: Int, height: Int, width: Int) = arrayOf(
    Pos(y + 1, x),
    Pos(y - 1, x),
    Pos(y, x + 1),
    Pos(y, x - 1)
).filter { validPos(it, height, width) }

fun validPos(pos: Pos, height: Int = Int.MAX_VALUE, width: Int = Int.MAX_VALUE) =
    pos.first in 0 until height && pos.second in 0 until width

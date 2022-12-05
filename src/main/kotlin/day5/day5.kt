package day5

import util.readInputForDay

data class Move(val amount: Int, val from: Int, val to: Int)

fun main() {
    val lines = readInputForDay(5)
    val towerSpec = lines.takeWhile { it.isNotBlank() }.dropLast(1)
    val towerCount = (lines[towerSpec.size].trim().length + 3)/4
    val moves = lines.drop(towerSpec.size + 2).map{ parseMove(it) }
    runCrateMover9000(buildTowers(towerSpec,towerCount), moves)
    runCrateMover9001(buildTowers(towerSpec,towerCount), moves)
}

fun runCrateMover9000(towers: MutableMap<Int, MutableList<Char>>, moves: List<Move>) {
    for((amount, from, to) in moves) {
        val sourceTower = towers.getValue(from)
        val chunk = sourceTower.takeLast(amount).reversed()
        towers[from] = sourceTower.dropLast(amount).toMutableList()
        towers.getValue(to).addAll(chunk)
    }
    print("CrateMover9000: ")
    println((1..towers.size).map { towers.getValue(it).last() }.joinToString(""))
}

fun runCrateMover9001(towers: MutableMap<Int, MutableList<Char>>, moves: List<Move>) {
    for((amount, from, to) in moves) {
        val sourceTower = towers.getValue(from)
        val chunk = sourceTower.takeLast(amount)
        towers[from] = sourceTower.dropLast(amount).toMutableList()
        towers.getValue(to).addAll(chunk)
    }
    print("CrateMover9001: ")
    println((1..towers.size).map { towers.getValue(it).last() }.joinToString(""))
}

fun parseMove(line: String): Move {
    val (amount, from, to) = line.split(" ").slice(listOf(1,3,5)).map { it.toInt() }
    return Move(amount, from, to)
}

fun buildTowers(towerSpec: List<String>, towerCount: Int): MutableMap<Int, MutableList<Char>> {
    val towers = mutableMapOf<Int, MutableList<Char>>()
    for(towerNum in 0 until towerCount) {
        val tower = mutableListOf<Char>()
        towers[1 + towerNum] = tower
        for (line in towerSpec) {
            tower.add(line[1 + towerNum * 4])
        }
        tower.removeIf{ it.isWhitespace() }
        tower.reverse()
    }
    return towers
}

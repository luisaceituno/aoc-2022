package day19

import util.readInputForDay
import kotlin.math.max

fun main() {
    val blueprints = readInputForDay(19).map { parseBlueprint(it) }
    part1(blueprints)
    part2(blueprints)
}

fun part1(blueprints: List<Blueprint>) {
    val best = HashMap<Blueprint, Int>()
    for (bp in blueprints) {
        var s = setOf(State(oreRobots = 1))
        var max = 0
        for (time in 24 downTo 1) {
            s = s.flatMapTo(HashSet()) { workAMinute(bp, it) }
            max = max(max, s.maxOf { it.geodes })
            s = s.filterTo(HashSet()) { it.canReach(max, time) }
        }
        best[bp] = s.maxOf { it.geodes }
        println("Blueprint ${bp.id} bruteforced: ${best[bp]}")
    }
    println("Part1: ${best.map { (k, v) -> k.id * v }.sum()}")
}

fun part2(blueprints: List<Blueprint>) {
    val best = HashMap<Blueprint, Int>()
    for (bp in blueprints.take(3)) {
        var s = setOf(State(oreRobots = 1))
        var max = 0
        for (time in 32 downTo 1) {
            s = s.flatMapTo(HashSet()) { workAMinute(bp, it) }
            max = max(max, s.maxOf { it.geodes })
            s = s.filterTo(HashSet()) { it.canReach(max, time) }
        }
        best[bp] = max
        println("Blueprint ${bp.id} bruteforced: ${best[bp]}")
    }
    println("Part2: ${best.values.reduce { a, b -> a * b }}")
}

fun workAMinute(bp: Blueprint, s: State): Set<State> {
    val options = bp.options(s)
    s.increment()
    return options.mapTo(HashSet()) { bp.build(s.copy(), it) }
}

fun parseBlueprint(line: String): Blueprint {
    val n = line
        .replace(":", "")
        .split(" ")
        .slice(listOf(1, 6, 12, 18, 21, 27, 30))
        .map { it.toInt() }
    return Blueprint(n[0], n[1], n[2], n[3], n[4], n[5], n[6])
}

enum class Option { None, Ore, Clay, Obs, Geode }

data class Blueprint(
    val id: Int,
    val oreRobotOre: Int,
    val clayRobotOre: Int,
    val obsidianRobotOre: Int,
    val obsidianRobotClay: Int,
    val geodeRobotOre: Int,
    val geodeRobotObsidian: Int
)

data class State(
    var oreRobots: Int = 0,
    var clayRobots: Int = 0,
    var obsidianRobots: Int = 0,
    var geodeRobots: Int = 0,
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geodes: Int = 0,
) {
    fun canReach(value: Int, inTime: Int): Boolean {
        return geodes + geodeRobots * inTime + (inTime * inTime + 1) / 2 > value
    }
}

fun State.increment() {
    ore += oreRobots
    clay += clayRobots
    obsidian += obsidianRobots
    geodes += geodeRobots
}

fun Blueprint.canBuild(s: State, option: Option): Boolean {
    return when (option) {
        Option.Geode -> s.ore >= geodeRobotOre && s.obsidian >= geodeRobotObsidian
        Option.Obs -> s.ore >= obsidianRobotOre && s.clay >= obsidianRobotClay
        Option.Clay -> s.ore >= clayRobotOre
        Option.Ore -> s.ore >= oreRobotOre
        else -> false
    }
}

fun Blueprint.options(s: State): Set<Option> {
    if (canBuild(s, Option.Geode)) return setOf(Option.Geode)
    val options = HashSet<Option>(4)
    options.add(Option.None)
    if (canBuild(s, Option.Obs)) options.add(Option.Obs)
    if (canBuild(s, Option.Clay)) options.add(Option.Clay)
    if (canBuild(s, Option.Ore)) options.add(Option.Ore)
    return options
}

fun Blueprint.build(s: State, opt: Option): State {
    if (opt == Option.Ore) {
        s.ore -= oreRobotOre
        s.oreRobots++
    } else if (opt == Option.Clay) {
        s.ore -= clayRobotOre
        s.clayRobots++
    } else if (opt == Option.Obs) {
        s.ore -= obsidianRobotOre
        s.clay -= obsidianRobotClay
        s.obsidianRobots++
    } else if (opt == Option.Geode) {
        s.ore -= geodeRobotOre
        s.obsidian -= geodeRobotObsidian
        s.geodeRobots++
    }
    return s
}
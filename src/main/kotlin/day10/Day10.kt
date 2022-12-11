package day10

import util.readInputForDay
import kotlin.math.abs

fun main() {
    val lines = readInputForDay(10)
    val state = State(1)
    for (line in lines) {
        if (line.startsWith("noop")) state.tick()
        else {
            val x = line.substring(5).toInt()
            state.tick()
            state.tick()
            state.add(x)
        }
    }
    part1(state)
    part2(state)
}

fun part1(state: State) {
    val result = state.history
        .mapIndexed { idx, v -> (idx + 1) * v }
        .slice(19..219 step 40)
        .sum()
    println("Part1: $result")
}

fun part2(state: State) {
    val lines = state.history
        .mapIndexed { idx, v -> if (abs(v - (idx % 40)) > 1) '.' else '#' }
        .chunked(40)
        .joinToString("\n") { it.joinToString("") }
    println("Part2:\n$lines")
}

class State(private var register: Int) {
    val history = mutableListOf<Int>()

    fun tick() {
        history.add(register)
    }

    fun add(x: Int) {
        register += x
    }
}

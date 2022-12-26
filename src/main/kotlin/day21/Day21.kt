package day21

import util.readInputForDay
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun main() {
    val lines = readInputForDay(21).filter { it.isNotBlank() }
    part1(parseMonkeys(lines))
    part2(parseMonkeys(lines))
}

fun part1(monkeys: Map<String, Monkey>) {
    println("Part1: ${monkeys.getValue("root").solve(monkeys).toLong()}")
}

fun part2(monkeys: Map<String, Monkey>) {
    val rootMonke = monkeys.getValue("root")
    val leftMonke = monkeys.getValue(rootMonke.left)
    val rightMonke = monkeys.getValue(rootMonke.right)
    val left1 = runYelling(leftMonke, 0.0, monkeys)
    val left2 = runYelling(leftMonke, 1.0, monkeys)
    val right1 = runYelling(rightMonke, 0.0, monkeys)
    val right2 = runYelling(rightMonke, 1.0, monkeys)
    val distance1 = (left1 - right1).abs()
    val distance2 = (left2 - right2).abs()
    val distanceDelta = distance2 - distance1
    val neededSteps = (-distance1 / distanceDelta).setScale(0, RoundingMode.HALF_UP)
    println("Part2: $neededSteps")
}

fun runYelling(start: Monkey, myValue: Double, monkeys: Map<String, Monkey>): BigDecimal {
    monkeys.values.forEach { it.reset() }
    monkeys.getValue("humn").setSolved(BigDecimal.valueOf(myValue))
    return start.solve(monkeys)
}

fun parseMonkeys(lines: List<String>): Map<String, Monkey> {
    return lines.map { spec ->
        val (name, yell) = spec.split(": ")
        val ops = yell.split(" ")
        Monkey(name, ops[0], ops.getOrElse(1) { "" }, ops.getOrElse(2) { "" })
    }.associateBy { it.name }
}

class Monkey(val name: String, val left: String, val op: String = "", val right: String = "") {
    var solved = false
    var value = BigDecimal.ZERO

    init {
        reset()
    }

    fun solve(monkeys: Map<String, Monkey>): BigDecimal {
        if (solved) return value
        val leftVal = monkeys.getValue(left).solve(monkeys)
        val rightVal = monkeys.getValue(right).solve(monkeys)
        solved = true
        value = applyOperation(leftVal, rightVal)
        return value
    }

    private fun applyOperation(leftVal: BigDecimal, rightVal: BigDecimal): BigDecimal {
        return when (op) {
            "+" -> leftVal + rightVal
            "*" -> leftVal * rightVal
            "-" -> leftVal - rightVal
            "/" -> leftVal.divide(rightVal, MathContext(100, RoundingMode.HALF_UP))
            else -> throw IllegalArgumentException()
        }
    }

    fun reset() {
        solved = op.isBlank()
        value = if (op.isBlank()) left.toBigDecimal() else BigDecimal.ZERO
    }

    fun setSolved(v: BigDecimal) {
        solved = true
        value = v
    }
}

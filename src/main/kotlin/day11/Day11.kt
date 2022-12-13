package day11

import util.readInputForDay
import java.math.BigInteger

fun main() {
    val lines = readInputForDay(11)
    val specs = lines.chunked(7)
    part1(specs.map { parseMonkey(it) })
    part2(specs.map { parseMonkey(it) })
}

private fun part1(monkeys: List<Monkey>) {
    for (round in 1..20) {
        monkeys.forEach { it.haveTurn(monkeys) }
    }
    val result = monkeys.map { it.getInspectionCount() }.sorted().takeLast(2).reduce { a, b -> a * b }
    println("Part1: $result")
}

private fun part2(monkeys: List<Monkey>) {
    val chillFactor = monkeys.map { it.divisor }.reduce{ a, b -> a * b }
    for (round in 1..10000) {
        monkeys.forEach { it.haveTurn(monkeys, chillFactor) }
    }
    val result = monkeys.map { it.getInspectionCount() }.sorted().takeLast(2).reduce { a, b -> a * b }
    println("Part2: $result")
}

fun parseMonkey(specLines: List<String>): Monkey {
    val items = specLines[1].split(":")[1].split(",").map { it.trim().toBigInteger() }
    val opSpec = specLines[2].split("= old ")[1]
    val divisor = specLines[3].split("by ")[1].toBigInteger()
    val targetTrue = specLines[4].split("monkey ")[1].toInt()
    val targetFalse = specLines[5].split("monkey ")[1].toInt()
    return Monkey(
        items.toMutableList(),
        parseOp(opSpec),
        divisor,
        targetTrue,
        targetFalse
    )
}

fun parseOp(opSpec: String): (x: BigInteger) -> BigInteger {
    if (opSpec.startsWith("* old")) return { x: BigInteger -> x.multiply(x) }
    if (opSpec.startsWith("+ old")) return { x: BigInteger -> x.add(x) }
    val operator = opSpec[0]
    val operand = opSpec.split(" ")[1].toBigInteger()
    if (operator == '*') return { x: BigInteger -> x.multiply(operand) }
    return { x: BigInteger -> x.add(operand) }
}

class Monkey(
    private val items: MutableList<BigInteger>,
    private val operation: (x: BigInteger) -> BigInteger,
    val divisor: BigInteger,
    private val targetTrue: Int,
    private val targetFalse: Int
) {
    private var inspectionCount = BigInteger.ZERO

    fun haveTurn(monkeyGroup: List<Monkey>, worryMitigator: BigInteger? = null) {
        while (items.isNotEmpty()) {
            inspectionCount++
            val item = items.removeFirst()
            val worried = operation(item)
            val chilled = if(worryMitigator == null) worried / BigInteger.valueOf(3) else worried % worryMitigator
            if (chilled % divisor == BigInteger.ZERO) {
                monkeyGroup[targetTrue].catch(chilled)
            } else {
                monkeyGroup[targetFalse].catch(chilled)
            }
        }
    }

    private fun catch(item: BigInteger) {
        items.add(item)
    }

    fun getInspectionCount(): BigInteger {
        return inspectionCount
    }

    override fun toString(): String {
        return "$inspectionCount, $items"
    }
}

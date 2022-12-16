package day13

import util.readInputForDay
import java.util.*

fun main() {
    val lines = readInputForDay(13).asSequence()
    val elements = lines.filter { it.isNotBlank() }.map { parseNextElement(it.toLinkedList()) }
    part1(elements)
    part2(elements)
}

private fun part1(elements: Sequence<Element>) {
    val pairs = elements.chunked(2)
    val comparisons = pairs.map { (e1, e2) -> e1.compareTo(e2) }
    val correctlyOrderedPairCount = comparisons
        .mapIndexed { idx, comp -> if (comp <= 0) idx + 1 else 0 }
        .sum()
    println("Part1: $correctlyOrderedPairCount")
}

private fun part2(elements: Sequence<Element>) {
    val divider1 = parseNextElement("[[2]]".toLinkedList())
    val divider2 = parseNextElement("[[6]]".toLinkedList())
    val extended = elements + arrayOf(divider1, divider2)
    val sorted = extended.sorted()
    val index1 = sorted.indexOf(divider1) + 1
    val index2 = sorted.indexOf(divider2) + 1
    println("Part2: ${index1 * index2}")
}

fun String.toLinkedList(): LinkedList<Char> {
    return LinkedList(this.toList())
}

fun parseNextElement(spec: Deque<Char>): Element {
    val start = spec.pop()
    if (start == '[') {
        val content = mutableListOf<Element>()
        while (spec.peek() != ']') {
            val e = parseNextElement(spec)
            content.add(e)
        }
        spec.pop()
        if (spec.peek() == ',') spec.pop()
        return Arr(content)
    }
    var nSpec = "" + start
    while (spec.peek().isDigit()) nSpec += spec.pop()
    if (spec.peek() == ',') spec.pop()
    return Num(nSpec.toInt())
}

sealed interface Element : Comparable<Element> {}

class Num(val value: Int) : Element {
    fun asArr(): Arr {
        return Arr(listOf(this))
    }

    override fun compareTo(other: Element): Int {
        if (other is Num) {
            return value.compareTo(other.value)
        }
        return asArr().compareTo(other)
    }
}

class Arr(val elements: List<Element>) : Element {
    override fun compareTo(other: Element): Int {
        return when (other) {
            is Num -> compareTo(other.asArr())
            is Arr -> {
                if (this.elements.isEmpty() && other.elements.isEmpty()) return 0
                if (this.elements.isEmpty()) return -1
                if (other.elements.isEmpty()) return 1
                return this.elements
                    .zip(other.elements)
                    .map { (e1, e2) -> e1.compareTo(e2) }
                    .firstOrNull { it != 0 }
                    ?: (this.elements.size - other.elements.size)
            }
        }
    }
}
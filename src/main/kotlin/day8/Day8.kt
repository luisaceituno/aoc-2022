package day8

import util.readInputForDay

fun main() {
    val lines = readInputForDay(8)
    val heights = lines.map { it.toCharArray().map { h -> h.digitToInt() } }
    part1(heights)
    part2(heights)
}

fun part1(heights: List<List<Int>>) {
    val width = heights[0].size
    val height = heights.size
    val cols = 1 until width - 1
    val rows = 1 until height - 1

    val visibles = Array(height) { IntArray(width) { 0 } }

    // horizontal scanning
    for (row in rows) {
        var maxL = heights[row][0]
        for (col in cols) {
            val height = heights[row][col]
            if (height > maxL) {
                visibles[row][col] = 1
                maxL = height
            }
        }
        var maxR = heights[row][width - 1]
        for (col in cols.reversed()) {
            val height = heights[row][col]
            if (height > maxR) {
                visibles[row][col] = 1
                maxR = height
            }
        }
    }

    // vertical scanning
    for (col in cols) {
        var maxUp = heights[0][col]
        for (row in rows) {
            val height = heights[row][col]
            if (height > maxUp) {
                visibles[row][col] = 1
                maxUp = height
            }
        }
        var maxDown = heights[height - 1][col]
        for (row in rows.reversed()) {
            val height = heights[row][col]
            if (height > maxDown) {
                visibles[row][col] = 1
                maxDown = height
            }
        }
    }

    val borderCount = 2 * width + 2 * height - 4
    val innerCount = visibles.sumOf { it.sum() }
    println("Part1: ${borderCount + innerCount}")
}

fun part2(heights: List<List<Int>>) {
    var max = 0
    val rows = 1 until heights.size - 1
    val cols = 1 until heights[0].size - 1
    val heightCols = heights[0].indices.map { col -> (heights.indices).map { row -> heights[row][col] } }
    for (y in rows) {
        for (x in cols) {
            val row = heights[y]
            val col = heightCols[x]
            val score = getLineScore(row, x) * getLineScore(col, y)
            if (score > max) {
                max = score
            }
        }
    }
    println("Part2: $max")
}

fun getLineScore(line: List<Int>, startPos: Int): Int {
    var r = 1
    var x = startPos
    while (++x < line.size - 1 && line[x] < line[startPos]) {
        r++
    }
    var l = 1
    x = startPos
    while (--x > 0 && line[x] < line[startPos]) {
        l++
    }
    return r * l
}

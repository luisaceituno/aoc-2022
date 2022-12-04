package util

import java.io.File
fun readInputForDay(day: Int): List<String> {
    return File("src/main/kotlin/day$day/input.txt").readLines()
}

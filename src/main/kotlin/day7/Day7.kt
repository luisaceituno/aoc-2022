package day7

import util.readInputForDay

fun main() {
    val lines = readInputForDay(7)
    val root = parse(lines)
    part1(root)
    part2(root)
}

fun part1(root: File) {
    val sum = root.flatten().filter { it.directory && it.getTotalSize() <= 100000  }.sumOf { it.getTotalSize() }
    println("Part1: $sum")
}

fun part2(root: File) {
    val free = 70_000_000 - root.getTotalSize()
    val needed = 30_000_000 - free
    val min = root.flatten().filter { it.directory }.map { it.getTotalSize() }.filter { it >= needed }.minOf { it }
    println("Part2: $min")
}

fun parse(lines: List<String>): File {
    val root = File("", 0, mutableListOf(), true, null)
    var wd = root
    for(line in lines) {
        if (line == "$ cd /") wd = root
        else if (line == "$ cd ..") wd = wd.parent ?: throw IllegalStateException()
        else if (line.startsWith("$ cd ")) wd = wd.getChild(line.substring(5))
        else if (line.startsWith("dir")) wd.addDir(line.substring(4))
        else if (line[0].isDigit()) {
            val (sSize, name) = line.split(" ")
            wd.addFile(name, sSize.toInt())
        }
    }
    return root
}

class File(
    var name: String,
    var selfSize: Int,
    var children: MutableList<File>,
    val directory: Boolean,
    var parent: File?
) {
    fun getTotalSize(): Int {
        return selfSize + children.sumOf { it.getTotalSize() }
    }

    fun getChild(name: String): File {
        return children.find { it.name == name } ?: throw IllegalArgumentException()
    }

    fun addDir(name: String) {
        children.add(File(name, 0, mutableListOf(), true, this))
    }

    fun addFile(name: String, size: Int) {
        children.add(File(name, size, mutableListOf(), false, parent))
    }

    fun flatten(): List<File> {
        return listOf(this) + this.children.flatMap { it.flatten() }
    }

    override fun toString(): String {
        return "$name [$selfSize]"
    }
}

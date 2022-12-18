package day17

import util.YX
import util.readInputForDay
import kotlin.math.abs

val minusRock = setOf(YX(0, 0), YX(0, 1), YX(0, 2), YX(0, 3))
val plusRock = setOf(YX(0, 1), YX(1, 0), YX(1, 1), YX(1, 2), YX(2, 1))
val jRock = setOf(YX(0, 2), YX(1, 2), YX(2, 0), YX(2, 1), YX(2, 2))
val lRock = setOf(YX(0, 0), YX(1, 0), YX(2, 0), YX(3, 0))
val dotRock = setOf(YX(0, 0), YX(0, 1), YX(1, 0), YX(1, 1))
val rockOrder = listOf(minusRock, plusRock, jRock, lRock, dotRock)

fun main() {
    val drafts = readInputForDay(17).first().toList().map { if (it == '<') -1 else 1 }
    part1(drafts)
    part2(drafts)
}

private fun part1(drafts: List<Int>) {
    val tower = Tower(drafts)
    var currentRock = 0
    for (n in 1..2022) {
        val rock = Rock(currentRock, rockOrder[currentRock])
        tower.addRock(rock)
        currentRock = ++currentRock % rockOrder.size
    }
    println("Part1: ${abs(tower.getHighestY())}")
}

private fun part2(drafts: List<Int>) {
    println("Now let's pray we can detect cycles in your tower...")
    val tower = Tower(drafts)
    var currentRock = 0
    while (!tower.hasCycle) {
        val rock = Rock(currentRock, rockOrder[currentRock])
        tower.addRock(rock)
        currentRock = ++currentRock % rockOrder.size
    }
    println("You're in luck! Cycle detected with same rock, tower configuration, and jet pattern position")
    val cycleLength = tower.cycleEnd.rockCount - tower.cycleStart.rockCount
    println("The cycle has a length of $cycleLength. Using shortcut!")
    val canAdvanceCycles = (1000000000000 - tower.rockCounter) / cycleLength
    tower.advanceCycles(canAdvanceCycles)
    while (tower.rockCounter < 1000000000000) {
        val rock = Rock(currentRock, rockOrder[currentRock])
        tower.addRock(rock)
        currentRock = ++currentRock % rockOrder.size
    }
    println("Part2: ${abs(tower.getHighestY())}")
}

class Tower(
    private val drafts: List<Int>,
    private val minLeft: Int = 0,
    private val maxRight: Int = 6,
    private val cycleDetectionRowsToConsider: Int = 10 // Seems to work reliably with 8+
) {
    var rockCounter = 0L
    var hasCycle = false
    var cycleStart = TrackedPoint(0, 0)
    var cycleEnd = TrackedPoint(0, 0)

    private var occupiedPoints = mutableMapOf<Long, MutableSet<Long>>()
    private var floor = 0L
    private var currentDraft = -1
    private val cycleTracker = HashMap<String, TrackedPoint>()

    fun getHighestY(): Long {
        return floor + getHighestYRelativeToFloor()
    }

    private fun getHighestYRelativeToFloor(): Long {
        return occupiedPoints.keys.minOrNull() ?: 0
    }

    fun addRock(rock: Rock) {
        rockCounter++
        rock.topLeft = YX(getHighestYRelativeToFloor() - 3 - rock.height, 2)
        var ableToMove = true
        // move the rock until it rests
        while (ableToMove) {
            currentDraft = ++currentDraft % drafts.size
            val draft = drafts[currentDraft]
            val nextSideways = rock.topLeft.move(right = draft.toLong())
            tryToMove(rock, nextSideways)
            val nextDown = rock.topLeft.move(down = 1)
            ableToMove = tryToMove(rock, nextDown)
        }
        // add rock points to our global points map
        for ((y, x) in rock.absolutePoints()) {
            val row = occupiedPoints.computeIfAbsent(y) { HashSet() }
            row.add(x)
        }
        // discard lower part of a tower if a row is full (and try to detect a cycle while at it)
        for (y in rock.absolutePoints().map { it.y }.toSortedSet()) {
            val row = occupiedPoints.getValue(y)
            if (row.size > maxRight - minLeft) {
                occupiedPoints.keys.filter { it >= y }.forEach { occupiedPoints.remove(it) }
                occupiedPoints = occupiedPoints.mapKeysTo(HashMap()) { it.key - y }
                floor += y
                break
            }
        }
        val state = calculateState()
        detectCycle(rock.id, state)
    }

    private fun tryToMove(rock: Rock, nextPos: YX): Boolean {
        val initialPos = rock.topLeft
        rock.topLeft = nextPos
        if (collides(rock)) {
            rock.topLeft = initialPos
            return false
        }
        return true
    }

    private fun collides(rock: Rock): Boolean {
        if (rock.left() < minLeft) return true
        if (rock.right() > maxRight) return true
        if (rock.bottom() >= 0) return true
        for ((y, x) in rock.absolutePoints()) {
            if (occupiedPoints[y]?.contains(x) == true) {
                return true
            }
        }
        return false
    }

    private fun calculateState(): String {
        val chars = ArrayList<Char>()
        for (rowPoints in occupiedPoints.entries
            .sortedBy { it.key }
            .take(cycleDetectionRowsToConsider)
            .map { it.value }
        ) {
            chars.addAll((minLeft..maxRight).map { x -> if (rowPoints.contains(x.toLong())) '1' else '0' })
        }
        return chars.joinToString("")
    }

    private fun detectCycle(rockId: Int, state: String) {
        val currentFingerprint = "$rockId.$currentDraft.$state"
        val currentPoint = TrackedPoint(rockCounter, getHighestY())
        if (cycleTracker.contains(currentFingerprint)) {
            hasCycle = true
            cycleStart = cycleTracker.getValue(currentFingerprint)
            cycleEnd = currentPoint
        } else {
            cycleTracker[currentFingerprint] = currentPoint
        }
    }

    fun advanceCycles(count: Long) {
        rockCounter += count * (cycleEnd.rockCount - cycleStart.rockCount)
        floor += count * (cycleEnd.height - cycleStart.height)
    }
}

class Rock(val id: Int, val points: Collection<YX>, var topLeft: YX = YX(0, 0)) {
    val height = points.maxOf { it.y } - points.minOf { it.y } + 1
    val width = points.maxOf { it.x } - points.minOf { it.x } + 1
    fun left(): Long {
        return topLeft.x
    }

    fun right(): Long {
        return topLeft.x + width - 1
    }

    fun bottom(): Long {
        return topLeft.y + height - 1
    }

    fun absolutePoints(): Set<YX> {
        return points.map { it.move(right = topLeft.x, down = topLeft.y) }.toSet()
    }
}

data class TrackedPoint(val rockCount: Long, val height: Long)
package day16

import util.readInputForDay

fun main() {
    val valves = readInputForDay(16).map { parseValve(it) }
    val valvesIdx = valves.associateBy { it.name }
    val part1 = Run(valvesIdx).start(listOf("AA"), 30)
    println("Part1: $part1")
    println("Now go get a coffee, this will take a while...")
    val part2 = Run(valvesIdx).start(listOf("AA", "AA"), 26)
    println("Part2: $part2")
}

fun parseValve(line: String): Valve {
    val (defSpec, connectionSpec) = line.split("; ")
    val (name, flowSpec) = defSpec.split(" ").slice(listOf(1, 4))
    val flow = flowSpec.split("=").last().toInt()
    val connections = connectionSpec.replace(",", "").split(" ").drop(4)
    return Valve(name, flow, connections)
}

class Valve(val name: String, val flow: Int, val connections: List<String>) {
    override fun toString(): String {
        return "Valve(name='$name', flow=$flow, connections=$connections)"
    }
}

class Run(private val valves: Map<String, Valve>) {
    private var max = -1
    private val maxByFingerprint = HashMap<Int, HashMap<String, Int>>()

    fun start(initialValves: List<String>, totalTime: Int): Int {
        val openAtStart = valves.values.filter { it.flow == 0 }.map { it.name }.toSet()
        val initial = Step(initialValves, 0, openAtStart, getPotential(openAtStart, totalTime - 1))
        move(initial, totalTime)
        return max
    }

    private fun record(time: Int, step: Step): Boolean {
        if (step.potential < max) {
            return false
        }
        if (step.totalFlow > max) max = step.totalFlow
        val timeTable = maxByFingerprint.computeIfAbsent(time) { HashMap() }
        val maxByFp = timeTable.getOrDefault(step.fingerprint, -1)
        if (step.totalFlow <= maxByFp) return false
        timeTable[step.fingerprint] = step.totalFlow
        return true
    }

    private fun getPotential(openValves: Set<String>, remainingTime: Int): Int {
        return valves.minus(openValves).values.sumOf { it.flow * remainingTime }
    }

    private fun move(step: Step, remainingTime: Int) {
        if (remainingTime == 0) return
        val nextSteps = getNextSteps(step, remainingTime - 1)
        for (next in nextSteps) {
            move(next, remainingTime - 1)
        }
    }

    private fun getNextSteps(source: Step, remainingTime: Int): Collection<Step> {
        val adjustedP = source.totalFlow + getPotential(source.openValves, remainingTime)
        val adjustedSource = Step(source.currentPositions, source.totalFlow, source.openValves, adjustedP)
        var possibleNext: Collection<Step> = listOf(adjustedSource)
        for (person in source.currentPositions) {
            possibleNext = getNextStepsForPerson(person, possibleNext, remainingTime)
        }
        return possibleNext
    }

    private fun getNextStepsForPerson(
        person: String,
        sources: Collection<Step>,
        remainingTime: Int,
    ): Collection<Step> {
        val valve = valves.getValue(person)
        val next = ArrayList<Step>(valve.connections.size + 1)
        for (step in sources) {
            if (!step.openValves.contains(person)) {
                val positions = step.currentPositions
                val flow = step.totalFlow + valve.flow * remainingTime
                val openValves = step.openValves + person
                val potential = flow + getPotential(openValves, remainingTime)
                val openStep = Step(positions, flow, openValves, potential)
                if (record(remainingTime, openStep)) next.add(openStep)
            }
            val nextBasePositions = step.currentPositions - person
            for (conn in valve.connections) {
                val nextPositions = nextBasePositions.toMutableList()
                nextPositions.add(conn)
                nextPositions.sort()
                val moveStep = Step(nextPositions, step.totalFlow, step.openValves, step.potential)
                if (record(remainingTime, moveStep)) next.add(moveStep)
            }
        }
        return next
    }
}

data class Step(
    val currentPositions: List<String>,
    val totalFlow: Int,
    val openValves: Set<String>,
    val potential: Int
) {
    val fingerprint = "${currentPositions.hashCode()}.${openValves.hashCode()}"
}


package de.jball.aoc2022.day16

import de.jball.aoc2022.Day

class Day16(test: Boolean = false): Day<Int>(test, 1652, 0) {
    private val valveRegex = "Valve (\\w+) has flow rate=(\\d+); tunnels lead to valves (\\w+(, \\w+)*)".toRegex()
    val valves = input
        .map { line -> parseValve(line) }
        .associateBy { valve -> valve.name }

    private fun parseValve(line: String): Valve {
        val groups = valveRegex.find(line)!!.groups
        return Valve(groups[1]!!.value, groups[2]!!.value.toInt(), groups[3]!!.value.split(", "), false)
    }

    override fun part1(): Int {
        TODO("Not yet implemented")
    }

    override fun part2(): Int {
        TODO("Not yet implemented")
    }
}

data class Valve(val name: String, val flowRate: Int, val neighborRefs: List<String>, var open: Boolean) {
    fun neighbors(valves: Map<String, Valve>) = neighborRefs.map {valves[it]!!}
}

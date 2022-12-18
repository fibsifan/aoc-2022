package de.jball.aoc2022.day16

import de.jball.aoc2022.Day

class Day16(test: Boolean = false): Day<Int>(test, 1651, 0) {
    private val valveRegex = "Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (\\w+(, \\w+)*)".toRegex()
    private val valves = input
        .map { line -> parseValve(line) }
        .associateBy { valve -> valve.name }
    private val start = parseValve(input[0])
    private val valvesWithImpact = valves
        .filter { (_, valve) -> valve.flowRate > 0 }
        .map { (name, valve) -> Pair(name, valve.flowRate) }
        .toMap()
    private val happenedStates = mutableMapOf<Pair<Set<String>, String>, Int>()

    private fun parseValve(line: String): Valve {
        val groups = valveRegex.find(line)!!.groups
        return Valve(groups[1]!!.value, groups[2]!!.value.toInt(), groups[3]!!.value.split(", "), false)
    }

    override fun part1(): Int {
        val initialState = State(setOf(), 0, start.name)
        var lastStates = setOf(initialState)
        for (i in 1..30) {
            lastStates = lastStates.map { lastState ->
                if (currentValveHasImpact(lastState) && currentValveIsOpen(lastState)) {
                    move(lastState).union(setOf(openVent(lastState)))
                } else {
                    move(lastState)
                }
            }.flatten().toSet()
        }
        return lastStates
            .maxBy { state -> state.ventedSteam }
            .ventedSteam
    }

    private fun currentValveIsOpen(lastState: State) = !lastState.openValves.contains(lastState.position)

    private fun currentValveHasImpact(lastState: State) = valvesWithImpact.containsKey(lastState.position)

    private fun openVent(current: State) = State(current.openValves.plus(current.position),
            current.ventedSteam + current.openValves.sumOf { valvesWithImpact[it]!! },
            current.position)
    private fun move(current: State): Set<State> {
        val newStates = valves[current.position]!!.neighborRefs
            .map { neighborRef ->
                State(current.openValves,
                    current.ventedSteam + current.openValves.sumOf { valve -> valvesWithImpact[valve]!! },
                    neighborRef)
            }
            .filter { state -> didntHappenOrWasWorse(state) }.toSet()

        newStates.forEach { newState ->
            happenedStates[Pair(newState.openValves, newState.position)] = newState.ventedSteam
        }

        return newStates
    }

    private fun didntHappenOrWasWorse(state: State) =
        !happenedStates.containsKey(Pair(state.openValves, state.position)) ||
            happenedStates[Pair(state.openValves, state.position)]!! < state.ventedSteam

    override fun part2(): Int {
        TODO("Not yet implemented")
    }
}

data class Valve(val name: String, val flowRate: Int, val neighborRefs: List<String>, var open: Boolean)

data class State(val openValves: Set<String>, val ventedSteam: Int, val position: String)

fun main() {
    Day16().run()
}

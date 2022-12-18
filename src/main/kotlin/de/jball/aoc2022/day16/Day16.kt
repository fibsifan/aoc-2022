package de.jball.aoc2022.day16

import de.jball.aoc2022.Day

class Day16(test: Boolean = false): Day<Int>(test, 1651, 1707) {
    private val valveRegex = "Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (\\w+(, \\w+)*)".toRegex()
    private val valves = input
        .map { line -> parseValve(line) }
        .associateBy { valve -> valve.name }
    private val start = valves["AA"]!!
    private val valvesWithImpact = valves
        .filter { (_, valve) -> valve.flowRate > 0 }
        .map { (name, valve) -> Pair(name, valve.flowRate) }
        .toMap()
    private val happenedStates = mutableMapOf<Pair<Set<String>, String>, Int>()
    private val happenedStatesWithElephant = mutableMapOf<Triple<Set<String>, String, String>, Int>()

    private fun parseValve(line: String): Valve {
        val groups = valveRegex.find(line)!!.groups
        return Valve(groups[1]!!.value, groups[2]!!.value.toInt(), groups[3]!!.value.split(", "), false)
    }

    override fun part1(): Int {
        val initialState = SingleState(setOf(), 0, start.name)
        var lastStates = setOf(initialState)
        for (i in 1..30) {
            lastStates = lastStates.map { lastState ->
                if (currentValveHasImpact(lastState) && currentValveIsNotOpen(lastState)) {
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

    private fun currentValveIsNotOpen(lastState: SingleState) = !lastState.openValves.contains(lastState.myPosition)
    private fun currentElephantValveIsNotOpen(lastState: StateWithElephant) = !lastState.openValves.contains(lastState.elephantPosition)

    private fun currentValveHasImpact(lastState: SingleState) = valvesWithImpact.containsKey(lastState.myPosition)
    private fun currentElephantValveHasImpact(lastState: StateWithElephant) = valvesWithImpact.containsKey(lastState.elephantPosition)

    private fun openVent(current: SingleState) = SingleState(current.openValves.plus(current.myPosition),
            current.ventedSteam + current.openValves.sumOf { valvesWithImpact[it]!! },
            current.myPosition)
    private fun openVent(current: StateWithElephant) = StateWithElephant(current.openValves.plus(current.myPosition),
        current.ventedSteam + current.openValves.sumOf { valvesWithImpact[it]!! },
        current.myPosition, current.elephantPosition)
    private fun openElephantVent(current: StateWithElephant) = StateWithElephant(current.openValves.plus(current.elephantPosition),
        current.ventedSteam,
        current.myPosition, current.elephantPosition)
    private fun move(current: SingleState): Set<SingleState> {
        val newStates = valves[current.myPosition]!!.neighborRefs
            .map { neighborRef ->
                SingleState(current.openValves,
                    current.ventedSteam + current.openValves.sumOf { valve -> valvesWithImpact[valve]!! },
                    neighborRef)
            }
            .filter { state -> didntHappenOrWasWorse(state) }.toSet()

        newStates.forEach { newState ->
            happenedStates[Pair(newState.openValves, newState.myPosition)] = newState.ventedSteam
        }

        return newStates
    }


    private fun move(current: StateWithElephant): Set<StateWithElephant> {
        val newStates = valves[current.myPosition]!!.neighborRefs
            .map { neighborRef ->
                StateWithElephant(current.openValves,
                    current.ventedSteam + current.openValves.sumOf { valve -> valvesWithImpact[valve]!! },
                    neighborRef,
                    current.elephantPosition)
            }
            .filter { state -> didntHappenOrWasWorse(state) }.toSet()

        newStates.forEach { newState ->
            happenedStatesWithElephant[Triple(newState.openValves, newState.myPosition, newState.elephantPosition)] = newState.ventedSteam
        }

        return newStates
    }

    private fun moveElephant(current: StateWithElephant): Set<StateWithElephant> {
        val newStates = valves[current.elephantPosition]!!.neighborRefs
            .map { neighborRef ->
                StateWithElephant(current.openValves,
                    current.ventedSteam,
                    current.myPosition, neighborRef)
            }
            .filter { stateWithElephant -> didntHappenOrWasWorse(stateWithElephant) }.toSet()

        newStates.forEach { newState ->
            happenedStatesWithElephant[Triple(newState.openValves, newState.myPosition, newState.elephantPosition)] = newState.ventedSteam
        }

        return newStates
    }

    private fun didntHappenOrWasWorse(state: SingleState) =
        !happenedStates.containsKey(Pair(state.openValves, state.myPosition)) ||
            happenedStates[Pair(state.openValves, state.myPosition)]!! < state.ventedSteam

    private fun didntHappenOrWasWorse(state: StateWithElephant) =
        !happenedStatesWithElephant.containsKey(Triple(state.openValves, state.myPosition, state.elephantPosition)) ||
            happenedStatesWithElephant[Triple(state.openValves, state.myPosition, state.elephantPosition)]!! < state.ventedSteam

    override fun part2(): Int {
        val initialState = StateWithElephant(setOf(), 0, start.name, start.name)
        var lastStates = setOf(initialState)
        for (i in 1..26) {
            lastStates = lastStates.map { lastState ->
                if (currentValveHasImpact(lastState) && currentValveIsNotOpen(lastState)) {
                    move(lastState).union(setOf(openVent(lastState)))
                } else {
                    move(lastState)
                }
            }.flatten().toSet()
            lastStates = lastStates.map { lastState ->
                if (currentElephantValveHasImpact(lastState) && currentElephantValveIsNotOpen(lastState)) {
                    moveElephant(lastState).union(setOf(openElephantVent(lastState)))
                } else {
                    moveElephant(lastState)
                }
            }.flatten().toSet()
        }
        return lastStates
            .maxBy { state -> state.ventedSteam }
            .ventedSteam
    }
}

data class Valve(val name: String, val flowRate: Int, val neighborRefs: List<String>, var open: Boolean)

open class SingleState(val openValves: Set<String>, val ventedSteam: Int, val myPosition: String) {
    override fun equals(other: Any?): Boolean {
        return other is SingleState && openValves == other.openValves && ventedSteam == other.ventedSteam && myPosition == other.myPosition
    }

    override fun hashCode(): Int {
        var result = openValves.hashCode()
        result = 31 * result + ventedSteam
        result = 31 * result + myPosition.hashCode()
        return result
    }
}
class StateWithElephant(openValves: Set<String>, ventedSteam: Int, myPosition: String, val elephantPosition: String) :
    SingleState(openValves, ventedSteam, myPosition) {

    override fun equals(other: Any?): Boolean {
        return other is StateWithElephant && super.equals(other) && other.elephantPosition == this.elephantPosition
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + elephantPosition.hashCode()
    }
}

fun main() {
    Day16().run()
}

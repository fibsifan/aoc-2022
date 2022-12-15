package de.jball.aoc2022.day03

import de.jball.aoc2022.Day

class Day03(test: Boolean = false) : Day<Long>(test, 157, 70) {
    private val letterMapping = (
        ('a'..'z').zip(1L..26L) +
            ('A'..'Z').zip(27L..52L))
        .toMap()

    private val rucksacks = input
        .map { it.toCharArray().toList() }

    override fun part1(): Long {
        return rucksacks
            .map { rucksack -> Pair(firstCompartment(rucksack), secondCompartment(rucksack)) }
            .map { (a, b) -> a.intersect(b) }
            .sumOf { letterMapping[it.first()]!! }
    }

    private fun firstCompartment(rucksack: List<Char>) =
        rucksack.subList(0, rucksack.size / 2).toSet()

    private fun secondCompartment(rucksack: List<Char>) =
        rucksack.subList(rucksack.size / 2, rucksack.size).toSet()

    override fun part2(): Long {
        return rucksacks
            .map { rucksack -> rucksack.toSet() }
            .chunked(3)
            .map { threeRucksacks -> threeRucksacks.reduce { a, b -> a.intersect(b) } }
            .sumOf { letterMapping[it.first()]!! }
    }
}

fun main() {
    Day03().run()
}

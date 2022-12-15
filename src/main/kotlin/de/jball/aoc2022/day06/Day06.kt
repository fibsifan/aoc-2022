package de.jball.aoc2022.day06

import de.jball.aoc2022.Day

class Day06(test: Boolean = false): Day<Int>(test, 7,19) {
    override fun part1(): Int {
        return input[0].windowed(4, 1)
            .mapIndexed { index, window -> Pair(index, window.toSet()) }
            .first { it.second.size == 4 }
            .first + 4
    }

    override fun part2(): Int {
        return input[0].windowed(14, 1)
            .mapIndexed { index, window -> Pair(index, window.toSet()) }
            .first { it.second.size == 14 }
            .first + 14
    }
}

fun main() {
    Day06().run()
}

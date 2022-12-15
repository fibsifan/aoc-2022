package de.jball.aoc2022.day05
import de.jball.aoc2022.Day
import java.util.Stack

class Day05(test: Boolean = false): Day<String>(test, "CMZ", "MCD") {
    private val crates1: List<Stack<Char>>
    private val crates2: List<Stack<Char>>

    init {
        val crateNumbers = input[input.indexOf("") - 1]
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
        val crateLines = input.subList(0,input.indexOf("")-1)
            .map { line ->
                line.chunked(4)
                    .map { crate -> crate.trim() }
                    .map { crate -> if (crate.matches("\\[\\w]".toRegex())) crate[1] else null }
            }
        crates1 = crateNumbers.map { Stack<Char>() }
        crates2 = crateNumbers.map { Stack<Char>() }
        crateLines.reversed()
            .forEach { list -> list
                .forEachIndexed { index, crate ->
                    if (crate != null) {
                        crates1[index].push(crate)
                        crates2[index].push(crate)
                    }
                }
            }
    }

    private val instructions = input.subList(input.indexOf("")+1, input.size)
        .map { Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!.groups }
        .map { groups -> (1..3).map { match -> groups[match]!!.value.toInt() } }

    override fun part1(): String {
        instructions.forEach { instruction -> repeat(instruction[0]) {
            crates1[instruction[2]-1].push(crates1[instruction[1]-1].pop())
        } }
        return crates1.map { it.pop() }.joinToString("")
    }

    override fun part2(): String {
        instructions.forEach { instruction -> (1..instruction[0])
            .map { crates2[instruction[1]-1].pop() }
            .reversed()
            .forEach { crates2[instruction[2]-1].push(it)}
        }
        return crates2.map { it.pop() }.joinToString("")
    }
}

fun main() {
    Day05().run()
}

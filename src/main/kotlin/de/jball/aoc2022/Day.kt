package de.jball.aoc2022
import java.io.File

abstract class Day<T>(
    private val test: Boolean = false,
    private val expected1: T,
    private val expected2: T) {

    val input: List<String> = File(
        "src/${if (test) "test" else "main"}/resources",
        "${this::class.simpleName}${if (test) "_test" else ""}.txt"
    ).readLines()

    abstract fun part1(): T
    abstract fun part2(): T
    fun run() {
        val part1 = part1()
        println(part1)
        if (test) {
            check(expected1 == part1)
        }

        val part2 = part2()
        println(part2)
        if (test) {
            check(expected2 == part2)
        }
    }
}

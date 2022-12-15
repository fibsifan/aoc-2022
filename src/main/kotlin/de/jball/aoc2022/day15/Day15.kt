package de.jball.aoc2022.day15

import de.jball.aoc2022.Day
import kotlin.math.abs

class Day15(test: Boolean = false) : Day<Long>(test, 26L, 56_000_011L) {
    private val parserRegex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    private val sensors = input.map { parseSensor(it) }
    private val beacons = input.map { parseBeacon(it) }.toSet()
    private val y = if (test) 10 else 2_000_000
    private val xylim = if (test) 20 else 4_000_000
    private val largestDistance = sensors.maxOf { (_, _, distance) -> distance }
    private val smallestX = sensors.minOf { (x, _, _) -> x }
    private val largestX = sensors.maxOf { (x, _, _) -> x }

    private fun parseSensor(line: String): Sensor {
        val (a, b, c, d) = (1..4).map { parserRegex.find(line)!!.groups[it]!!.value.toInt() }
        return Sensor(a, b, manhattanDistance(Position(a, b), Position(c, d)))
    }

    private fun parseBeacon(line: String): Beacon {
        val (a, b) = (3..4).map { parserRegex.find(line)!!.groups[it]!!.value.toInt() }
        return Beacon(a, b)
    }

    private fun manhattanDistance(a: Point, b: Point) = abs(a.x - b.x) + abs(a.y - b.y)

    override fun part1(): Long {
        val notDistressBeacon = (smallestX - largestDistance..largestX + largestDistance)
            .map { x -> Position(x, y) }
            .count { sensors.any { (x, y, radius) -> manhattanDistance(Position(x, y), it) <= radius } }
        val beaconsOnY = beacons.count { (_, beaconY) -> y == beaconY }
        return (notDistressBeacon - beaconsOnY).toLong()
    }

    override fun part2(): Long {
        val (ascendingLines, descendingLines) = sensors.flatMap { it.lines() }.partition { line -> line.slope == 1 }
        ascendingLines.forEach { ascendingLine ->
            descendingLines.forEach { descendingLine ->
                val cross = crossPoint(ascendingLine, descendingLine)
                if (inGrid(cross) && sensors.none { manhattanDistance(cross, it) <= it.radius }) {
                    return cross.x.toLong() * 4_000_000L + cross.y.toLong()
                }
            }
        }
        error("No candidate found")
    }

    private fun inGrid(cross: Position) = cross.x in 0..xylim && cross.y in 0..xylim

    private fun crossPoint(ascendingLine: Line, descendingLine: Line): Position {
        // x+b == -x+d => x = (d-b) / 2
        val x = (descendingLine.ySection - ascendingLine.ySection) / 2
        val y = x + ascendingLine.ySection
        return Position(x, y)
    }

}

sealed class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
    override fun equals(other: Any?): Boolean {
        return other is Point && x == other.x && y == other.y
    }

    override fun hashCode() = 31*x+y
    override fun toString() = "($x,$y)"
}

class Beacon(x: Int, y: Int) : Point(x, y)
class Sensor(x: Int, y: Int, val radius: Int) : Point(x, y) {
    operator fun component3() = radius
    // lines just right outside the manhattan distance
    private val upperAscendingLine = Line(1, y-x+radius+1)
    private val lowerAscendingLine = Line(1, y-x-radius-1)
    private val upperDescendingLine = Line(-1, y+x+radius+1)
    private val lowerDescendingLine = Line(-1, y+x-radius-1)

    fun lines() = listOf(upperAscendingLine, lowerAscendingLine, upperDescendingLine, lowerDescendingLine)
}

class Position(x: Int, y: Int): Point(x, y)

data class Line(val slope: Int, val ySection: Int)

fun main() {
    Day15().run()
}

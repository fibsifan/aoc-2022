package de.jball.aoc2022.day14

import de.jball.aoc2022.Day
import kotlin.math.abs

class Day14(test: Boolean = false): Day<Int>(test, 24, 93) {
    private val rocks = input.map { rockFormation ->
        rockFormation.split(" -> ")
            .map { coordinate ->
                val (x, y) = coordinate.split(",")
                Point(x.toInt(), y.toInt())
            }
            .windowed(2)
            .flatMap { (a, b) -> a..b }
    }.flatten().toSet()
    private val restingGrains = mutableSetOf<Point>()

    private val lowestRock = rocks.maxBy { (_, b) -> b }.y
    private val dropPoint = Point(500,0)

    override fun part1(): Int {
        while(true) {
            var grain = dropPoint
            var nextPosition = nextPosition(grain)
            while (nextPosition != null && nextPosition.y <= lowestRock) {
                grain = nextPosition
                nextPosition = nextPosition(grain)
            }
            if (grain.y >= lowestRock) {
                return restingGrains.size
            }
            restingGrains.add(grain)
        }
    }

    private fun nextPosition(grain: Point): Point? {
        for (candidate in listOf(Point(grain.x, grain.y+1), Point(grain.x-1, grain.y+1), Point(grain.x+1, grain.y+1))) {
            if (!rocks.contains(candidate) && !restingGrains.contains(candidate)) {
                return candidate
            }
        }
        return null
    }

    override fun part2(): Int {
        while(true) {
            var grain = dropPoint
            var nextPosition = nextPosition(grain)
            while (nextPosition != null && nextPosition.y <= lowestRock+1) {
                grain = nextPosition
                nextPosition = nextPosition(grain)
            }
            restingGrains.add(grain)
            if (grain == dropPoint) {
                return restingGrains.size
            }
        }
    }
}

class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y

    operator fun rangeTo(other: Point): List<Point> {
        return if (x == other.x) {
            List(abs(other.y-y) + 1) { x }
                .zip(if(y < other.y) y..other.y else y downTo other.y)
                .map {(a, b) -> Point(a, b) }
        } else if (y == other.y) {
            (if(x < other.x) x..other.x else x downTo other.x)
                .zip(List(abs(other.x - x) + 1) { y })
                .map {(a, b) -> Point(a, b) }
        } else {
            error("range from $this to $other is not horizontally or vertically.")
        }
    }

    override fun toString(): String {
        return "$x,$y"
    }

    override fun hashCode(): Int {
        return ((3 * x.hashCode()) + 5) * y.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Point && other.x == x && other.y == y
    }
}

fun main() {
    Day14().run()
}

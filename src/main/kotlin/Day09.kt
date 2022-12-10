import kotlin.math.abs
import kotlin.math.sign

class Day09(test: Boolean = false) : Day<Int>(test, 88, 36) {
    private val directions = input.map { parseDirectionLine(it) }

    private fun parseDirectionLine(directionLine: String): Pair<Direction, Int> {
        val blah = directionLine.split(" ")
        return Pair(parseDirection(blah[0]), blah[1].toInt())
    }

    private fun parseDirection(directionString: String): Direction {
        return when (directionString) {
            "U" -> Direction.UP
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            "D" -> Direction.DOWN
            else -> {
                error("Should not happen")
            }
        }
    }

    override fun part1(): Int {
        val rope = Rope(2)

        return directions.map { rope.move(it.first, it.second) }
            .flatten().toSet().size
    }

    override fun part2(): Int {
        val rope = Rope(10)
        return directions.map { rope.move(it.first, it.second) }
            .flatten().toSet().size
    }
}

enum class Direction(val x: Int, val y: Int) {
    RIGHT(1, 0), LEFT(-1, 0), DOWN(0, -1), UP(0, 1)
}

class Rope(length: Int) {
    private val knots = MutableList(length) { Pair(0,0) }

    /**
     * @return tail-trail of that movement
     */
    fun move(direction: Direction, steps: Int): List<Pair<Int, Int>> {
        val tailTrail = mutableListOf<Pair<Int, Int>>()
        repeat(steps) {
            val head = knots[0]
            knots[0] = Pair(head.first + direction.x, head.second + direction.y)
            followHead()
            tailTrail.add(knots.last())
        }
        return tailTrail
    }

    private fun followHead() {
        for (i in 1 until knots.size) {
            val leader = knots[i-1]
            val knot = knots[i]
            knots[i] = if (abs(leader.first - knot.first) > 1 || abs(leader.second - knot.second) > 1) {
                Pair(
                    reduceDistanceIfAboveLength(leader.first, knot.first),
                    reduceDistanceIfAboveLength(leader.second, knot.second)
                )
            } else {
                knot
            }
        }
    }

    private fun reduceDistanceIfAboveLength(a: Int, b: Int) = b + (a - b).sign
}

fun main() {
    Day09().run()
}

package de.jball.aoc2022.day12

import de.jball.aoc2022.Day
import java.util.PriorityQueue

class Day12(test: Boolean = false): Day<Long>(test, 31, 29) {
    private val areaMap = input.mapIndexed { line, letters ->
        letters.chunked(1)
            .mapIndexed { column, letter ->
                MountainPosition(Pair(line, column), letter.toCharArray()[0], Int.MAX_VALUE.toLong())
            }
    }

    private val start = areaMap.flatten().first { pos -> pos.height == 'S' }
    private val end = areaMap.flatten().first { pos -> pos.height == 'E' }

    private fun calculatePaths(queue: PriorityQueue<MountainPosition>, reverse: Boolean = false) {
        while (queue.isNotEmpty()) {
            val current = queue.poll()!!
            if (!current.pathLengthIsMinimal) {
                getReachableNeighbors(current.position, reverse)
                    .also { neighbors ->
                        current.updateOwnDistance(neighbors, reverse)
                    }
                    .filter { !it.pathLengthIsMinimal }
                    .forEach { neighbor ->
                        neighbor.updateOwnDistance(listOf(current), reverse)
                        queue.add(neighbor)
                    }
                current.pathLengthIsMinimal = true
            }
        }
    }

    private fun getReachableNeighbors(position: Pair<Int, Int>, reverse: Boolean = false): List<MountainPosition> {
        return position.let { (line, column) ->
            neighborCandidates(line, column)
                .filter { next ->
                    if (reverse)
                        heightDifference(next, position) <= 1
                    else
                        heightDifference(position, next) <= 1
                }
                .map { areaMap[it.first][it.second] }
        }
    }

    private fun neighborCandidates(line: Int, column: Int) =
        listOf(Pair(line + 1, column), Pair(line - 1, column), Pair(line, column + 1), Pair(line, column - 1))
            .filter { next -> next.first in areaMap.indices && next.second in areaMap[0].indices }

    private fun heightDifference(current: Pair<Int, Int>, next: Pair<Int, Int>) =
        heightDifference(areaMap[current.first][current.second], areaMap[next.first][next.second])

    private fun heightDifference(current: MountainPosition, next: MountainPosition) =
        heightDifference(current.height, next.height)

    override fun part1(): Long {
        resetPathLengths()

        start.pathLength = 0
        // this is key to being fast (see wikipedia on Dijkstra):
        val queue = PriorityQueue( compareBy { pos: MountainPosition -> pos.pathLength })
        queue.add(start)

        calculatePaths(queue)

        return end.pathLength
    }

    override fun part2(): Long {
        resetPathLengths()

        end.pathLength = 0
        // this is key to being fast (see wikipedia on Dijkstra):
        val queue = PriorityQueue( compareBy { pos: MountainPosition -> pos.pathLength })
        queue.add(end)

        calculatePaths(queue, true)

        return areaMap.flatten().filter {it.height == 'a'}.minOf {it.pathLength}
    }

    private fun resetPathLengths() {
        areaMap.forEach {
            it.forEach { mountainPosition ->
                mountainPosition.pathLength = Int.MAX_VALUE.toLong()
                mountainPosition.pathLengthIsMinimal = false
            }
        }
    }
}

fun heightDifference(currentHeight: Char, nextHeight: Char) = actualHeight(nextHeight) - actualHeight(currentHeight)

private fun actualHeight(height: Char) = when (height) {
    'S' -> 'a'
    'E' -> 'z'
    else -> height
}


class MountainPosition(
    val position: Pair<Int, Int>,
    val height: Char,
    var pathLength: Long,
    var pathLengthIsMinimal: Boolean = false
) {
    fun updateOwnDistance(neighbors: List<MountainPosition>, reverse: Boolean = false) {
        val newMin = neighbors
            .filter { neighbor ->
                if (reverse)
                    heightDifference(height, neighbor.height) <= 1
                else
                    heightDifference(neighbor.height, height) <= 1
            }
            .minOfOrNull { it.pathLength + 1 }
        if (newMin != null && newMin < pathLength) {
            pathLength = newMin
        }
    }
}

fun main() {
    Day12().run()
}

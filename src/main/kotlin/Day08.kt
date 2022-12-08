import kotlin.math.max

class Day08(test: Boolean = false) : Day<Int>(test, 21, 8) {
    private val grid = input
        .mapIndexed { lineNo, line ->
            line.chunked(1)
                .mapIndexed { colNo, col -> Pair(Pair(lineNo, colNo), col.toInt()) }
        }
    private val lines = input.size

    override fun part1(): Int {
        val fromTop = grid.fold(initialListOfMaximums()) { folded, nextLine ->
            folded.zip(nextLine) { currentMax, currentValue ->
                Pair(
                    if (currentMax.second < currentValue.second) currentMax.first + listOf(currentValue.first) else currentMax.first,
                    max(currentMax.second, currentValue.second)
                )
            }
        }.flatMap { it.first }.toSet()


        val fromBottom = grid.reversed().fold(initialListOfMaximums()) { folded, nextLine ->
            folded.zip(nextLine) { currentMax, currentValue ->
                Pair(
                    if (currentMax.second < currentValue.second) currentMax.first + listOf(currentValue.first) else currentMax.first,
                    max(currentMax.second, currentValue.second)
                )
            }
        }.flatMap { it.first }.toSet()

        val fromLeft = grid.map { line ->
            line.fold(Pair(listOf<Pair<Int, Int>>(), Int.MIN_VALUE)) { currentMax, currentValue ->
                Pair(
                    if (currentMax.second < currentValue.second) currentMax.first + listOf(currentValue.first) else currentMax.first,
                    max(currentMax.second, currentValue.second)
                )
            }.first
        }.flatten().toSet()

        val fromRight = grid.map { line ->
            line.reversed().fold(Pair(listOf<Pair<Int, Int>>(), Int.MIN_VALUE)) { currentMax, currentValue ->
                Pair(
                    if (currentMax.second < currentValue.second) currentMax.first + listOf(currentValue.first) else currentMax.first,
                    max(currentMax.second, currentValue.second)
                )
            }.first
        }.flatten().toSet()

        return fromBottom.union(fromTop).union(fromLeft).union(fromRight).count()
    }

    private fun initialListOfMaximums() = List(lines) {
        Pair(listOf<Pair<Int, Int>>(), Int.MIN_VALUE)
    }

    override fun part2(): Int {
        TODO()
    }
}

fun main() {
    Day08().run()
}

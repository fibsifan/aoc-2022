import kotlin.math.max

class Day08(test: Boolean = false) : Day<Int>(test, 21, 8) {
    private val grid = input
        .mapIndexed { lineNo, line ->
            line.chunked(1)
                .mapIndexed { colNo, col -> Pair(Pair(lineNo, colNo), col.toInt()) }
        }
    private val gridMap = grid.flatten().toMap()
    private val lines = input.size
    private val columns = input[0].length

    override fun part1(): Int {
        val fromTop = grid.fold(emptyListsOfMaximums()) { listsOfMaximums, nextLine ->
            listsOfMaximums.zip(nextLine) { previousMaximumsInDirection, currentValue ->
                appendCurrentValueIfLarger(previousMaximumsInDirection, currentValue)
            }
        }.flatMap { it.first }.toSet()


        val fromBottom = grid.reversed().fold(emptyListsOfMaximums()) { listsOfMaximums, nextLine ->
            listsOfMaximums.zip(nextLine) { previousMaximumsInDirection, currentValue ->
                appendCurrentValueIfLarger(previousMaximumsInDirection, currentValue)
            }
        }.flatMap { it.first }.toSet()

        val fromLeft = grid.map { line ->
            line.fold(emptyListOfMaximums()) { previousMaximumsInDirection, currentValue ->
                appendCurrentValueIfLarger(previousMaximumsInDirection, currentValue)
            }.first
        }.flatten().toSet()

        val fromRight = grid.map { line ->
            line.reversed().fold(emptyListOfMaximums()) { previousMaximumsInDirection, currentValue ->
                appendCurrentValueIfLarger(previousMaximumsInDirection, currentValue)
            }.first
        }.flatten().toSet()

        return fromBottom.union(fromTop).union(fromLeft).union(fromRight).count()
    }

    private fun appendCurrentValueIfLarger(
        currentMax: Pair<List<Pair<Int, Int>>, Int>,
        currentValue: Pair<Pair<Int, Int>, Int>
    ) = Pair(
        if (currentMax.second < currentValue.second) currentMax.first + listOf(currentValue.first) else currentMax.first,
        max(currentMax.second, currentValue.second)
    )

    private fun emptyListsOfMaximums() = List(lines) {
        emptyListOfMaximums()
    }

    private fun emptyListOfMaximums() = Pair(listOf<Pair<Int, Int>>(), Int.MIN_VALUE)

    override fun part2(): Int {
        return grid.maxOf { line ->
            line.maxOf { (pos, height) ->
                val scenicScore = linesOfSight(pos).map { lineOfSight ->
                    val visibleTrees = lineOfSight.takeWhile { otherTree ->
                        gridMap[otherTree]!! < height
                    }
                    if (visibleTrees.size < lineOfSight.size) visibleTrees.size+1 else visibleTrees.size
                }.reduce {a, b -> a*b}
                scenicScore
            }
        }
    }

    private fun linesOfSight(treePosition: Pair<Int, Int>): List<List<Pair<Int, Int>>> {
        val (x, y) = treePosition
        return listOf((0 until x).reversed().zip(List(x) { y }),
            (x+1 until lines).zip(List(lines-x){ y }),
            List(y) { x }.zip((0 until y).reversed()),
            List(columns-y) { x }.zip((y+1 until columns)))
    }
}

fun main() {
    Day08().run()
}

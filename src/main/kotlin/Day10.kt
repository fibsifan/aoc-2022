
class Day10(test: Boolean = false): Day<Int>(test, 13140, 1 /*cheating here...*/) {
    private val program = input.map { parseCommand(it) }
    private val spritePositions = program
    .runningFold(listOf(Pair(0,1))) { state, operation -> operation.apply(state) }
    .flatten()
    .toMap()

    private fun parseCommand(line: String): Operation {
        return if (line == "noop") Noop else Add(line.split(" ")[1].toInt())
    }

    override fun part1(): Int {
        return (20 until spritePositions.size step 40)
            .sumOf {
                // -1, because of "during"
                spritePositions[it-1]!! * it
            }
    }

    override fun part2(): Int {
        for (i in (0 until 6)) {
            for (j in (0 until 40)) {
                print(pixel(i, j))
            }
            println()
        }
        return 1
    }

    private fun pixel(i: Int, j: Int): String {
        val spritePosition = spritePositions[i * 40 + j]!!
        return if (spritePosition >= j-1 && spritePosition <= j+1) "#" else "."
    }
}

sealed class Operation {
    abstract fun apply(previous: List<Pair<Int, Int>>): List<Pair<Int, Int>>
}

object Noop : Operation() {
    override fun apply(previous: List<Pair<Int, Int>>) = listOf(Pair(previous.last().first+1, previous.last().second))
}

class Add(private val v: Int): Operation() {
    override fun apply(previous: List<Pair<Int, Int>>) = listOf(
        Pair(previous.last().first+1, previous.last().second),
        Pair(previous.last().first+2, previous.last().second+v))
}

fun main() {
    Day10().run()
}

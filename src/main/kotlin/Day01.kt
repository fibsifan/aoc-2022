class Day01(test: Boolean = false) : Day<Long>(test, 24000, 45000) {
    private val elves: List<List<Long>> = getElves()

    private fun getElves(): List<List<Long>> {
        return input.joinToString(" ")
            .split("  ")
            .map { elven ->
                elven.split(" ")
                    .map { cal -> cal.toLong() }
            }
    }

    override fun part1(): Long {
        return elves.maxOf { it.sum() }
    }

    override fun part2(): Long {
        return elves.sortedBy { it.sum() }.reversed().subList(0, 3).sumOf { it.sum() }
    }
}

fun main() {
    Day01().run()
}

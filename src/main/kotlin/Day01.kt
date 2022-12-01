class Day01(test: Boolean = false) : Day(test, 24000, 45000) {
    private val elves: List<List<Long>> = getElves()

    private fun getElves(): List<List<Long>> {
        val mutableElves = mutableListOf<MutableList<Long>>()
        mutableElves.add(mutableListOf())
        for (bla in input) {
            if (bla.isBlank()) {
                mutableElves.add(mutableListOf())
            } else {
                mutableElves.last().add(bla.toLong())
            }
        }
        return mutableElves
    }
    override fun part1(): Long {
        return elves.maxOf { it.sum() }
    }

    override fun part2(): Long {
        return elves.sortedBy { it.sum() }.reversed().subList(0,3).sumOf { it.sum() }
    }
}

fun main() {
    Day01(false).run()
}
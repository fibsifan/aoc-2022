class Day04(test: Boolean = false) : Day(test, 2, 4) {
    private val sectionPairs = input
        .map { line ->
            val matchGroups = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)").matchEntire(line)!!.groups
            Pair(
                getNumber(matchGroups[1]!!)..getNumber(matchGroups[2]!!),
                getNumber(matchGroups[3]!!)..getNumber(matchGroups[4]!!)
            )
        }

    private fun getNumber(matchGroup: MatchGroup) = matchGroup.value.toInt()
    override fun part1(): Long {
        return sectionPairs
            .count { it.first.minus(it.second).isEmpty() || it.second.minus(it.first).isEmpty() }
            .toLong()
    }

    override fun part2(): Long {
        return sectionPairs
            .count { it.first.intersect(it.second).isNotEmpty() }
            .toLong()
    }
}

fun main() {
    Day04().run()
}

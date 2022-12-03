class Day03(test: Boolean = false) : Day(test, 157, 70) {
    val letterMapping = (
            ('a'..'z').zip(1L..26L) +
            ('A'..'Z').zip(27L..52L))
        .toMap()


    val rucksacks = input
        .map { it.toCharArray().toList() }

    override fun part1(): Long {
        return rucksacks
            .map { Pair(it.subList(0, it.size/2), it.subList(it.size/2, it.size)) }
            .map { it.first.toSet().intersect(it.second.toSet()) }
            .sumOf { letterMapping[it.first()]!! }
    }

    override fun part2(): Long {
        return rucksacks.chunked(3).map {
            it.map { rucksack -> rucksack.toSet() }
        }.map {
            it.reduce {a, b -> a.intersect(b)}
        }.sumOf {
            letterMapping[it.first()]!!
        }
    }
}

fun main() {
    Day03().run()
}
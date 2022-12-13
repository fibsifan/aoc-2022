class Day13(test: Boolean = false) : Day<Int>(test, 13, 0) {
    val pairs = input.chunked(3).map {Pair(parseLine(it[0]), parseLine(it[1])) }

    private fun parseLine(line: String): Packet {
        TODO("Not yet implemented")
    }

    override fun part1(): Int {
        return pairs.mapIndexed { index, pair -> Pair(index, pair) }
            .filter { (_, pair) -> pair.first <= pair.second }
            .sumOf {(index, _) -> index}
    }

    override fun part2(): Int {
        TODO("Not yet implemented")
    }
}

sealed class Packet: Comparable<Packet>

class ListPacket(val packets: List<Packet>): Packet() {
    init {
        TODO()
    }

    override fun compareTo(other: Packet): Int {
        TODO("Not yet implemented")
    }
}

class NumberPacket(val value: Int): Packet() {
    override fun compareTo(other: Packet): Int {
        if (other is NumberPacket) {
            return value.compareTo(other.value)
        } else {
            TODO()
        }
    }
}

fun main() {
    Day13().run()
}

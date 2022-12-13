import kotlin.math.min

class Day13(test: Boolean = false) : Day<Int>(test, 13, 0) {
    private val pairs = input.chunked(3).map {Pair(parsePacket(it[0]), parsePacket(it[1])) }

    private fun parsePacket(packetString: String): Packet {
        var workingString = packetString
        val context = mutableListOf<MutableList<Packet>>()
        while (workingString.isNotEmpty()) {
            if (workingString.startsWith("[")) {
                val startedList = mutableListOf<Packet>()
                context.add(startedList)
                workingString = workingString.substring(1)
            } else if (workingString.startsWith("]")) {
                val finishedList = ListPacket(context.removeLast())
                val parent = context.lastOrNull()
                if (parent != null) {
                    parent.add(finishedList)
                } else {
                    return finishedList
                }
            } else if (workingString.startsWith(",")) {
                workingString = workingString.substring(1)
            } else if (workingString matches "^\\d.*".toRegex()) {
                context.last().add(NumberPacket(workingString.substring(0,1).toInt()))
                workingString = workingString.substring(1)
            } else {
                error("Unexpected input $workingString")
            }
        }
        error("Should have returned in while loop")
    }

    override fun part1(): Int {
        return pairs.mapIndexed { index, pair -> Pair(index, pair) }
            .filter { (_, pair) ->
                pair.first <= pair.second
            }
            .sumOf {(index, _) -> index+1}
    }

    override fun part2(): Int {
        TODO("Not yet implemented")
    }
}

sealed class Packet: Comparable<Packet>

class ListPacket(val packets: List<Packet>): Packet() {
    override fun compareTo(other: Packet): Int {
        if (other is ListPacket) {
            for (i in 0 until min(packets.size, other.packets.size)) {
                val componentComparison = packets[i].compareTo(other.packets[i])
                if (componentComparison != 0) {
                    return componentComparison
                }
            }
            return packets.size.compareTo(other.packets.size)
        } else {
            return compareTo(ListPacket(listOf(other)))
        }
    }
}

class NumberPacket(val value: Int): Packet() {
    override fun compareTo(other: Packet): Int {
        return if (other is NumberPacket) {
            value.compareTo(other.value)
        } else {
            ListPacket(listOf(this)).compareTo(other)
        }
    }
}

fun main() {
    Day13().run()
}

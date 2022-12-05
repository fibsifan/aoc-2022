import java.util.Stack

class Day05(test: Boolean = false): Day<String>(test, "CMZ", "MCD") {
    private val crates: List<Stack<Char>>

    init {
        val tmp = input.subList(0,input.indexOf("")-1)
            .map { line ->
                line.chunked(4)
                    .map { crate -> crate.trim() }
                    .map { crate -> if (crate.matches("\\[\\w]".toRegex())) crate[1] else null }
            }
        crates = tmp[0].map { Stack<Char>() }
        tmp.reversed().forEach { list -> list.forEachIndexed { index, crate -> if (crate != null) crates[index].push(crate) }}
    }

    private val instructions = input.subList(input.indexOf("")+1, input.size)
        .map { Regex("move (\\d+) from (\\d+) to (\\d+)").find(it)!!.groups }
    override fun part1(): String {
        instructions.forEach { instruction -> repeat(getNumber(instruction, 1)) {
            crates[getNumber(instruction, 3)-1].push(crates[getNumber(instruction, 2)-1].pop())
        } }
        return crates.map { it.pop() }.joinToString("")
    }

    private fun getNumber(instruction: MatchGroupCollection, position: Int) = instruction[position]!!.value.toInt()

    override fun part2(): String {
        TODO("Not yet implemented")
    }
}

fun main() {
    Day05().run()
}

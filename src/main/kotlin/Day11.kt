class Day11(test: Boolean = false) : Day<Long>(test, 10605L, 2713310158L) {
    private val monkeys = input
        .chunked(7)
        .map { parseMonkey(it) }
    private val monkeys2 = input
        .chunked(7)
        .map { parseMonkey(it) }
    private val inspectionCounters2 = MutableList(monkeys2.size) { 0L }
    private val inspectionCounters = MutableList(monkeys.size) { 0L }

    private fun parseMonkey(monkeyData: List<String>): Monkey {
        val items = monkeyData[1].split(": ")[1].split(", ").map { it.toInt() }.toMutableList()

        val operationData = monkeyData[2].split(" = old ")[1]
        val operand = operationData.substringAfterLast(" ")
        val operation = if (operationData.startsWith("+"))
            fun (old: Int): Int {return old + if (operand=="old") old else operand.toInt()}
        else fun(old: Int): Int {return old * if (operand=="old") old else operand.toInt()}
        val divisor = monkeyData[3].substringAfterLast(" ").toInt()
        val targetIfDivisible = monkeyData[4].substringAfterLast(" ").toInt()
        val targetIfNotDivisible = monkeyData[5].substringAfterLast(" ").toInt()
        return Monkey(items, operation, divisor, Pair(targetIfDivisible, targetIfNotDivisible))
    }

    override fun part1(): Long {
        for (i in 1..20) {
            monkeys.forEachIndexed { index, monkey ->
                inspectionCounters[index] += monkey.items.size.toLong()
                monkey.turn(monkeys, 0)
            }
        }
        inspectionCounters.sortDescending()
        return inspectionCounters[0]*inspectionCounters[1]
    }

    override fun part2(): Long {
        monkeys2.forEach { monkey -> monkey.round2 = true }
        for (i in 1..10000) {
            monkeys2.forEachIndexed { index, monkey ->
                inspectionCounters2[index] += monkey.items.size.toLong()
                monkey.turn(monkeys2, monkeys2.map { it.divisor }.reduce {a, b -> a*b})
            }
        }
        inspectionCounters2.sortDescending()
        return inspectionCounters2[0]*inspectionCounters2[1]
    }
}

class Monkey(
    val items: MutableList<Int>,
    private val operation: (old: Int) -> Int,
    val divisor: Int,
    private val targets: Pair<Int, Int>,
    var round2: Boolean = false
) {
    fun turn(monkeys: List<Monkey>, modulus: Int) {
        while (items.isNotEmpty()) {
            val item = items.removeFirst()
            val target = inspect(item, modulus)
            monkeys[target.first].items.add(target.second)
        }
    }

    private fun inspect(item: Int, modulus: Int): Pair<Int, Int> {
        val newWorryLevel = if (round2) operation(item)%modulus else operation(item)/3
        return Pair(targetMonkey(newWorryLevel), newWorryLevel)
    }

    private fun targetMonkey(newWorryLevel: Int): Int {
        return if (newWorryLevel % divisor == 0) targets.first else targets.second
    }
}

fun main() {
    Day11().run()
}

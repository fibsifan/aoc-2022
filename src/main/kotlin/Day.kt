import java.io.File

abstract class Day(val test: Boolean = false, val expected1: Long = 0, val expected2: Long = 0) {
    val input: List<String> = File("src/${if(test) "test" else "main"}/resources",
            "${this::class.simpleName}${if (test) "_test" else ""}.txt")
            .readLines()

    abstract fun part1(): Long
    abstract fun part2(): Long
    fun run() {
        val part1 = part1()
        println(part1)
        if (test) {
            check(expected1 == part1)
        }

        val part2 = part2()
        println(part2)
        if (test) {
            check(expected2 == part2)
        }
    }
}

class Day02(test: Boolean = false): Day(test, 15, 12) {
    private val games: List<Pair<Played, ToPlay>> = input.map {parseGame(it) }

    private fun parseGame(it: String) = Pair(Played.valueOf(it.substringBefore(" ")), ToPlay.valueOf(it.substringAfter(" ")))
    override fun part1(): Long {
        return games.sumOf { evaluateGame(it.first, it.second) }
    }

    private fun evaluateGame(played: Played, toPlay: ToPlay): Long {
        return played.ordinal.toLong() + 1 + gameResult(played, toPlay)
    }

    private fun gameResult(played: Played, toPlay: ToPlay): Long {
        return ((toPlay.ordinal.toLong() - played.ordinal.toLong() + 4) % 3) * 3
    }

    override fun part2(): Long {
        return games.sumOf { anticipateGame(it.first, it.second) }
    }

    private fun anticipateGame(played: Played, toPlay: ToPlay):Long {
        return toPlay.ordinal.toLong() * 3 + selectSign(played, toPlay)
    }

    private fun selectSign(played: Played, toPlay: ToPlay): Long {
        return when(toPlay) {
            ToPlay.X -> (played.ordinal.toLong() +2 ) % 3 + 1
            ToPlay.Y -> played.ordinal.toLong() + 1
            ToPlay.Z -> (played.ordinal.toLong() + 1) % 3 + 1
        }
    }

    enum class Played {
        A, B, C
    }

    enum class ToPlay {
        X, Y, Z
    }
}

fun main() {
    Day02(false).run()
}
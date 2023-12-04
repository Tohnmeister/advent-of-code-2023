import kotlin.math.pow

fun main() {
    data class Game(val winningNumbers: List<Int>, val cardNumbers: List<Int>) {
        var count = 1

        val matchingNumbers: Set<Int> by lazy { cardNumbers.intersect(winningNumbers.toSet()) }

        fun points(): Int {
            if (matchingNumbers.isEmpty()) {
                return 0
            }

            return 2.0.pow(matchingNumbers.size - 1).toInt()
        }
    }

    fun toGame(line: String): Game {
        val colonPos = line.indexOf(':')
        val barPos = line.indexOf('|')
        val winningNumbersStr = line.substring(colonPos + 1, barPos)
        val cardNumbersStr = line.substring(barPos + 1)

        val winningNumbers = winningNumbersStr.split(' ').asSequence().filter { it.isNotEmpty() }.map{ it.toInt() }.toList()
        val cardNumbers = cardNumbersStr.split(' ').asSequence().filter { it.isNotEmpty() }.map { it.toInt() }.toList()

        return Game(winningNumbers, cardNumbers)
    }

    fun toGames(lines: List<String>) = lines.map { toGame(it) }

    fun part1(lines: List<String>): Int {
        val games = toGames(lines)
        return games.sumOf { it.points() }
    }

    fun part2(lines: List<String>): Int {
        val games = toGames(lines)
        for (idx in games.indices) {
            val game = games[idx]
            val nrMatches = game.matchingNumbers.size
            for (nextGame in games.asSequence().drop(idx + 1).take(nrMatches)) {
                nextGame.count += game.count
            }
        }

        return games.sumOf { it.count }
    }

    val lines = readLines("Day04.txt")

    part1(lines).println()
    part2(lines).println()
}

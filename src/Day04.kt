import kotlin.math.pow

fun main() {
    data class Game(val winningNumbers: List<Int>, val cardNumbers: List<Int>) {
        fun points(): Int {
            val matchingNumbers = cardNumbers.intersect(winningNumbers.toSet())

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

    val lines = readLines("Day04.txt")

    part1(lines).println()
}

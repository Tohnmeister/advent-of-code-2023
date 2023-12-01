fun main() {
    fun firstAndLastDigit(line: String): Int {
        val first = line.first { it.isDigit() }
        val last = line.last { it.isDigit() }
        return "$first$last".toInt()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { firstAndLastDigit(it) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("Day01.txt")

    part1(input).println()
    part2(input).println()
}

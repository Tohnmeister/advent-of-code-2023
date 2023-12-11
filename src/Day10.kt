fun main() {
    fun part1(lines: List<String>): Int {
        // Find S
        // Then from S, find the loop, by
        // Going in each direction, in which there's a connecting pipe, while keeping track of all visited positions
            // If a position is out of the field, then his is NOT the loop
            // If a position is visited twice, which is not S, then this is NOT the loop
            // If a position is visited twice, and it is S, then this is the loop
                // Return length of loop / 2
        val startPos = lines.asSequence().withIndex().flatMap { (x, line) -> line.withIndex().map { (y, cell) -> Pair(cell, Pair(x, y)) }}.find { it.first == 'S' }!!.second

        return lines.size
    }

    val lines = readLines("Day10.txt")

    part1(lines).println()
}

fun main() {

    data class Pipe(val start: Boolean = false, val top: Boolean = false, val right: Boolean = false, val bottom: Boolean = false, val left: Boolean = false)

    val pipeMap = mapOf(
        '|' to Pipe(top = true, bottom = true),
        '-' to Pipe(left = true, right = true),
        'L' to Pipe(top = true, right = true),
        'J' to Pipe(top = true, left = true),
        '7' to Pipe(bottom = true, left = true),
        'F' to Pipe(bottom = true, right = true),
        '.' to Pipe(),
        'S' to Pipe(start = true),
    )

    fun part1(lines: List<String>): Int {
        // Find S
        // Then from S, find the loop, by
        // Going in each direction, in which there's a connecting pipe, while keeping track of all visited positions
            // If a position is out of the field, then his is NOT the loop
            // If a position is visited twice, which is not S, then this is NOT the loop
            // If a position is visited twice, and it is S, then this is the loop
                // Return length of loop / 2
        val pipes = lines.map { line -> line.map { pipeMap[it]!! }}
        val startPos = pipes.asSequence().withIndex().flatMap { (x, line) -> line.withIndex().map { (y, pipe) -> Pair(pipe, Pair(x, y)) }}.find { it.first.start }!!.second
        val found = false
        var nrSteps = 0
        var currentPos = startPos
        while (!found) {
            val nextPositions = listOf(
                Pair(startPos.first, startPos.second - 1), Pair(startPos.first + 1, startPos.second), Pair(startPos.first, startPos.second + 1), Pair(startPos.first - 1, startPos.second)
            ).filter { (x, y) ->
                x >= 0 && x < pipes[0].size && y >= 0 && y < pipes.size
            }
            for (nextPos in nextPositions) {

            }
            ++nrSteps
        }

        return lines.size
    }

    val lines = readLines("Day10.txt")

    part1(lines).println()
}

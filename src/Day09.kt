fun main() {
    fun calculatePrevNext(sequence: List<Int>): Pair<Int, Int> {
        if (sequence.all { it == 0 }) {
            return Pair(0, 0)
        }

        val prevNext = calculatePrevNext(sequence.windowed(2).map { it[1] - it[0] })

        return Pair(sequence.first() - prevNext.first, sequence.last() + prevNext.second)
    }

    fun part1(sequences: List<List<Int>>): Int {
        return sequences.sumOf { calculatePrevNext(it).second }
    }

    fun part2(sequences: List<List<Int>>): Int {
        return sequences.sumOf { calculatePrevNext(it).first }
    }

    val lines = readLines("Day09.txt")
    val sequences = lines.map { line -> line.split(' ').map { str -> str.toInt() }}
    println(sequences)

    part1(sequences).println()
    part2(sequences).println()
}

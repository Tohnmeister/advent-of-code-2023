import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun calculateEmptyLineIndices(lines: List<String>): List<Int> {
        val emptyLineIndices =
            lines.withIndex().asSequence().filter { (_, line) -> line.all { char -> char == '.' } }.map { it.index }
                .toList().reversed()
        return emptyLineIndices
    }

    fun calculateEmptyColumnIndices(lines: List<String>) =
        lines[0].indices.filter { colIdx -> lines.all { line -> line[colIdx] == '.' } }.reversed()

    fun expand(lines: List<String>): List<List<Char>> {
        val emptyLineIndices = calculateEmptyLineIndices(lines)
        val emptyColumnIndices = calculateEmptyColumnIndices(lines)
        val mutableLines = lines.asSequence().map { it.toMutableList() }.toMutableList()

        for (lineIdx in emptyLineIndices) {
            mutableLines.add(lineIdx, MutableList(lines[0].length) { '.' })
        }

        for (colIdx in emptyColumnIndices) {
            for (line in mutableLines) {
                line.add(colIdx, '.')
            }
        }

        return mutableLines
    }

    fun findGalaxyPositions(lines: List<List<Char>>): List<Pair<Int, Int>> {
        return lines.withIndex().flatMap { (rowIdx, line) -> line.withIndex().filter { (_, char) -> char == '#' }.map { (colIdx) -> Pair(colIdx, rowIdx) }}
    }

    fun calculateManhattanDistance(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Int {
        return abs(pos1.first - pos2.first) + abs(pos1.second - pos2.second)
    }

    fun calculateManhattanDistance(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>, expansion: Int, emptyLineIndices: List<Int>, emptyColumnIndices: List<Int>): Long {
        val maxX = max(pos1.first, pos2.first)
        val minX = min(pos1.first, pos2.first)
        val maxY = max(pos1.second, pos2.second)
        val minY = min(pos1.second, pos2.second)

        val nrOfEmptyCols = ((minX + 1) until maxX).count { emptyColumnIndices.contains(it) }
        val nrOfEmptyRows = ((minY + 1) until maxY).count { emptyLineIndices.contains(it) }

        return calculateManhattanDistance(pos1, pos2).toLong() + nrOfEmptyCols * (expansion - 1) + nrOfEmptyRows * (expansion - 1)
    }

    fun part1(lines: List<String>): Int {
        val expandedLines = expand(lines)
        val galaxyPositions = findGalaxyPositions(expandedLines)
        return galaxyPositions.withIndex().sumOf { (idx, pos) -> galaxyPositions.drop(idx + 1).sumOf { calculateManhattanDistance(pos, it) } }
    }

    fun part2(lines: List<String>): Long {
        val emptyLineIndices = calculateEmptyLineIndices(lines)
        val emptyColumnIndices = calculateEmptyColumnIndices(lines)
        val galaxyPositions = findGalaxyPositions(lines.map{ it.toList() })

        return galaxyPositions.withIndex().sumOf { (idx, pos) ->
            galaxyPositions.drop(idx + 1).sumOf {
                calculateManhattanDistance(pos, it, 1_000_000, emptyLineIndices, emptyColumnIndices)
            }
        }
    }

    val lines = readLines("Day11.txt")

    val part1 = part1(lines)
    check(part1 == 9329143)
    part1.println()

    val part2 = part2(lines)
    part2.println()
}
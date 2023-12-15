import kotlin.math.pow

fun main() {
    fun calculateNrOfPossibilities(springList: String, sizeList: List<Int>): Int {
        println("Checking $springList")
        val regex =
            ("\\.*" + sizeList.map { List(it) { '#' } }.joinToString("\\.+") { it.joinToString("") } + "\\.*").toRegex()
        val nrOfDamagedSprings = springList.count { it == '#' }
        val nrOfMissingDamagedSprings = sizeList.sum() - nrOfDamagedSprings

        val unknownPositions =
            springList.asSequence().withIndex().filter { (_, char) -> char == '?' }.map { (idx, _) -> idx }.toList()

        val max = (2.0.pow(unknownPositions.size) - 1).toInt()

        var lineCount = 0

        for (i in 0..max) {
            val binary = i.toString(2)
            if (binary.count { it == '1' } == nrOfMissingDamagedSprings) {
                val stringBuilder = StringBuilder(springList.replace('?', '.'))

                val springPositions =
                    binary.reversed().asSequence().withIndex().filter { it.value == '1' }.map { it.index }.toList()
                for (springPos in springPositions) {
                    stringBuilder.setCharAt(unknownPositions[springPos], '#')
                }
                if (regex.matches(stringBuilder.toString())) {
                    ++lineCount
                }
            }
        }
        return lineCount
    }

    fun part1(lines: List<String>): Int {
        return lines.sumOf { line ->
            val spacePos = line.indexOf(' ')
            val springList = line.take(spacePos)
            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }
            calculateNrOfPossibilities(springList, sizeList)
        }
    }

    fun part2(lines: List<String>): Int {
        return lines.sumOf { line ->
            val spacePos = line.indexOf(' ')
            val springList = line.take(spacePos)
            val springListUnfolded = List(5) { springList }.joinToString("?")
            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }
            val sizeListUnfolded = List(5) { sizeList }.flatten()
            calculateNrOfPossibilities(springListUnfolded, sizeListUnfolded)
        }
    }

    val lines = readLines("Day12.txt")

    val part1 = part1(lines)
    check(part1 == 7771)
    part1.println()
    part2(lines).println()
}

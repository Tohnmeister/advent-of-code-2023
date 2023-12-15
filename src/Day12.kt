import kotlin.math.pow

fun main() {
    fun calculateSizeGroups(possibility: String): List<Int> {
        val result: MutableList<Int> = mutableListOf()
        var lastCount: Int? = null
        for (char in possibility) {
            if (char == '#') {
                if (lastCount == null) {
                    lastCount = 1
                } else {
                    ++lastCount
                }
            } else {
                if (lastCount != null) {
                    result.add(lastCount)
                    lastCount = null
                }
            }
        }
        if (lastCount != null) {
            result.add(lastCount)
        }

        return result
    }

    fun calculateNrOfPossibilities(springList: String, sizeList: List<Int>): Int {
//        println("Checking $springList")
//        val regex = ("\\.*" + sizeList.map { List(it) { '#' } }.joinToString("\\.+") { it.joinToString("") } + "\\.*").toRegex()
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
                val possibility = stringBuilder.toString()
                val actualSizeGroups = calculateSizeGroups(possibility)
                if (actualSizeGroups == sizeList) {
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
    part1.println()
    check(part1 == 7771)
    part2(lines).println()
}

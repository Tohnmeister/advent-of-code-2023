import java.util.stream.Collectors

fun main() {
    fun calculateNrOfPossibilities(springList: String, sizeList: List<Int>, currentSizeIdx: Int = 0): Long {
        val nrOfExpectedDamagedSprings = sizeList.asSequence().drop(currentSizeIdx).sum() // asSequence
        val nrOfDamagedSprings = springList.count { it == '#' }
        if (nrOfDamagedSprings > nrOfExpectedDamagedSprings) {
            return 0
        }
        val currentSize = sizeList[currentSizeIdx]
        val postSizes = sizeList.asSequence().drop(currentSizeIdx + 1) // asSequence
        val end = springList.length - 1 - postSizes.sumOf { it + 1 }
        val lastStart = end - currentSize + 1

        return (0..lastStart).sumOf { startPos ->
            val partBefore = springList.asSequence().take(startPos) // asSequence
            val currentPart = springList.asSequence().drop(startPos).take(currentSize) // asSequence
            val endPos = startPos + currentSize - 1
            val charAfter = if (endPos == springList.length - 1) null else springList[endPos + 1]
            val nextCharValid = charAfter != '#'

            if (nextCharValid && partBefore.none { it == '#' } && currentPart.none { it == '.' }) {
                if (currentSizeIdx == sizeList.size - 1) {
                    // If there are no more #s behind, then this is a valid possibility. Otherwise, it is not.
                    if (springList.asSequence().drop(startPos + currentSize + 1).none { it == '#' }) {
                        1
                    } else {
                        0
                    }
                } else {
                    calculateNrOfPossibilities(
                        springList.drop(startPos + currentSize + 1),
                        sizeList,
                        currentSizeIdx + 1
                    )
                }
            } else {
                0
            }
        }
    }

    fun part1(lines: List<String>): Long {
        return lines.sumOf { line ->
            val spacePos = line.indexOf(' ')
            val springList = line.take(spacePos)
            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }

            calculateNrOfPossibilities(springList, sizeList)
        }
    }

    fun part2(lines: List<String>): Long {
//        return lines.withIndex().sumOf { (idx, line) ->
//            val spacePos = line.indexOf(' ')
//            val springList = line.take(spacePos)
//            val springListUnfolded = List(5) { springList }.joinToString("?")
//            println("${idx + 1}/${lines.size}: $springListUnfolded")
//            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }
//            val sizeListUnfolded = List(5) { sizeList }.flatten()
//            calculateNrOfPossibilities(springListUnfolded, sizeListUnfolded)
//        }
        return lines.withIndex().toList().parallelStream().map { (idx, line) ->
            val spacePos = line.indexOf(' ')
            val springList = line.take(spacePos)
            val springListUnfolded = List(5) { springList }.joinToString("?")
            println("${idx + 1}/${lines.size}: $springListUnfolded")
            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }
            val sizeListUnfolded = List(5) { sizeList }.flatten()
            calculateNrOfPossibilities(springListUnfolded, sizeListUnfolded)
        }.collect(Collectors.summingLong { it })
    }

    val lines = readLines("Day12.txt")

    val part1 = part1(lines)
    part1.println()
    check(part1 == 7771L)
    part2(lines).println()
}

fun main() {
    data class CacheKey(val springList: String, val sizeList: List<Int>, val currentSizeIdx: Int)

    val cache: MutableMap<CacheKey, Long> = mutableMapOf()

    fun calculateNrOfPossibilities(springList: String, sizeList: List<Int>, currentSizeIdx: Int = 0): Long {
        return cache.getOrPut(CacheKey(springList, sizeList, currentSizeIdx)) {
            val currentSize = sizeList[currentSizeIdx]
            val postSizes = sizeList.asSequence().drop(currentSizeIdx + 1)
            val end = springList.length - 1 - postSizes.sumOf { it + 1 }
            val lastStart = end - currentSize + 1

            (0..lastStart).sumOf { startPos ->
                val partBefore = springList.asSequence().take(startPos)
                val currentPart = springList.asSequence().drop(startPos).take(currentSize)
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
        return lines.withIndex().sumOf { (idx, line) ->
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
    check(part1 == 7771L)
    val part2 = part2(lines)
    part2.println()
    check(part2 == 10861030975833)
}

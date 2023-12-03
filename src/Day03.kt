import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(lines: List<String>): Int {
        fun isPartNumber(row: Int, startPos: Int, endPos: Int): Boolean {
            val startRow = max(0, row - 1)
            val endRow = min(lines.size - 1, row + 1)
            val startCol = max(0, startPos - 1)
            val endCol = min(lines[0].length - 1, endPos + 1)

            for (rowNr in startRow..endRow) {
                for (colNr in startCol..endCol) {
                    val curChar = lines[rowNr][colNr]
                    if (!curChar.isDigit() && curChar != '.') {
                        return true
                    }
                }
            }
            return false
        }

        var sum = 0
        for (lineNr in lines.indices) {
            var colNr = 0
            while (colNr < lines[0].length) {
                if (lines[lineNr][colNr].isDigit()) {
                    val numberStr = lines[lineNr].drop(colNr).takeWhile { it.isDigit() }
                    val number = numberStr.toInt()

                    if (isPartNumber(lineNr, colNr, colNr + numberStr.length - 1)) {
                        sum += number
                    }

                    colNr += numberStr.length
                } else {
                    ++colNr
                }

            }
        }
        return sum
    }

    val lines = readLines("Day03.txt")

    part1(lines).println()
}

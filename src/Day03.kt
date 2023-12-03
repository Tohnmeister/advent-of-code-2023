import kotlin.math.max
import kotlin.math.min

data class FieldInfo(val partNumbers: List<PartNumber>, val gearIcons: List<GearIcon>)

data class PartNumber(val value: Int, val rowNr: Int, val startPos: Int, val endPos: Int) {
    fun isAdjacentTo(gearIcon: GearIcon): Boolean {
        return gearIcon.rowNr >= rowNr - 1 && gearIcon.rowNr <= rowNr + 1 && gearIcon.colNr >= startPos - 1 && gearIcon.colNr <= endPos + 1
    }
}

data class GearIcon(val rowNr: Int, val colNr: Int) {
    fun gearRatio(partNumbers: List<PartNumber>): Int {
        val gears = partNumbers.filter { it.isAdjacentTo(this) }
        return if (gears.size == 2) {
            gears[0].value * gears[1].value
        } else {
            0
        }
    }
}

fun main() {
    fun fieldInfo(lines: List<String>): FieldInfo {
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

        val partNumbers = mutableListOf<PartNumber>()
        val gearIcons = mutableListOf<GearIcon>()
        for (lineNr in lines.indices) {
            var colNr = 0
            while (colNr < lines[0].length) {
                if (lines[lineNr][colNr].isDigit()) {
                    val numberStr = lines[lineNr].drop(colNr).takeWhile { it.isDigit() }
                    val number = numberStr.toInt()

                    val endPos = colNr + numberStr.length - 1
                    if (isPartNumber(lineNr, colNr, endPos)) {
                        partNumbers.add(PartNumber(number, lineNr, colNr, endPos))
                    }

                    colNr += numberStr.length
                } else {
                    if (lines[lineNr][colNr] == '*') {
                        gearIcons.add(GearIcon(lineNr, colNr))
                    }

                    ++colNr
                }

            }
        }
        return FieldInfo(partNumbers, gearIcons)
    }

    fun part1(lines: List<String>): Int {
        val partNumbers = fieldInfo(lines).partNumbers
        return partNumbers.sumOf { it.value }
    }

    fun part2(lines: List<String>): Int {
        val fieldInfo = fieldInfo(lines)
        return fieldInfo.gearIcons.sumOf { it.gearRatio(fieldInfo.partNumbers) }
    }

    val lines = readLines("Day03.txt")

    val part1Result = part1(lines)
    check(part1Result == 527144) // To allow refactoring, while working on part 2

    part1Result.println()
    part2(lines).println()
}

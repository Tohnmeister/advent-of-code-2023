import kotlin.math.pow

fun main() {
    fun part1(lines: List<String>): Int {
        fun isValid(springList: String, sizeList: List<Int>): Boolean {
            val regexStr = "\\.*" + sizeList.map { List(it) { '#' } }.joinToString("\\.+") { it.joinToString("") } + "\\.*"
            val regex = regexStr.toRegex()

            val result = regex.matches(springList)

//            if (result) {
//                println("$springList matches $regex")
//            } else {
//                println("$springList does not match $regex")
//            }

            return result
        }

        var count = 0

        for (line in lines) {
            val spacePos = line.indexOf(' ')
            val springList = line.take(spacePos)
            val sizeList = line.drop(spacePos + 1).split(',').map { it.toInt() }

            val unknownPositions = springList.asSequence().withIndex().filter { (_, char) -> char == '?' }.map { (idx, _) -> idx }.toList()

            val max = (2.0.pow(unknownPositions.size) - 1).toInt()

            var lineCount = 0

            for (i in 0..max) {
                val binary = i.toString(2)
                val stringBuilder = StringBuilder(springList.replace('?', '.'))

                val springPositions = binary.reversed().asSequence().withIndex().filter { it.value == '1' }.map { it.index }.toList()
                for (springPos in springPositions) {
                    stringBuilder.setCharAt(unknownPositions[springPos], '#')
                }
                if (isValid(stringBuilder.toString(), sizeList)) {
                    ++lineCount
                }
            }
            count += lineCount
        }

        return count
    }

    val lines = readLines("Day12.txt")

    part1(lines).println()
}

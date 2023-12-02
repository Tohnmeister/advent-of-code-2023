fun main() {
    fun part1(input: List<String>): Int {
        fun firstAndLastDigit(line: String): Int {
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            return "$first$last".toInt()
        }

        return input.sumOf { firstAndLastDigit(it) }
    }

    fun part2(input: List<String>): Int {
        fun firstAndLastDigitWithText(line: String): Int {
            fun toInt(text: String): Int {
                if (text.length == 1) {
                    return text[0].digitToInt()
                }

                return when (text) {
                    "one" -> 1
                    "two" -> 2
                    "three" -> 3
                    "four" -> 4
                    "five" -> 5
                    "six" -> 6
                    "seven" -> 7
                    "eight" -> 8
                    "nine" -> 9
                    else -> throw IllegalArgumentException("Can't convert $text")
                }
            }

            val validValues = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            val first = line.findAnyOf(validValues)
            val last = line.findLastAnyOf(validValues)
            return "${toInt(first!!.second)}${toInt(last!!.second)}".toInt()
        }

        return input.sumOf { firstAndLastDigitWithText(it) }
    }

    val input = readLines("Day01.txt")

    part1(input).println()
    part2(input).println()
}

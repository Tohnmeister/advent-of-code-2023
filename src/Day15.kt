fun main() {
    fun calculateHash(step: String): Int {
        var currentValue = 0
        for (char in step) {
            val asciiValue = char.code
            currentValue += asciiValue
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }

    fun part1(lines: List<String>): Int {
        val steps = lines[0].split(',')

        return steps.sumOf { calculateHash(it) }
    }

    val lines = readLines("Day15.txt")
//    val lines = readLines("Day15Test.txt")

    part1(lines).println()
}

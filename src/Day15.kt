fun main() {
    fun calculateHash(step: String): Int = step.fold(0) { currentValue, char -> ((currentValue + char.code) * 17) % 256 }

    fun part1(lines: List<String>): Int {
        val steps = lines[0].split(',')

        return steps.sumOf { calculateHash(it) }
    }

    val lines = readLines("Day15.txt")
//    val lines = readLines("Day15Test.txt")

    val part1 = part1(lines)
    check(part1 == 511215)
    part1.println()
}
